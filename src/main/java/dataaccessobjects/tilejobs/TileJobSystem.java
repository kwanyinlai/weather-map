package dataaccessobjects.tilejobs;

import dataaccessinterface.TileNotFoundException;
import dataaccessobjects.CachedTileRepository;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class TileJobSystem {
    private final LinkedBlockingDeque<TileJob> queue = new LinkedBlockingDeque<>();
    private final Set<TileJob> processingJobs = Collections.synchronizedSet(new HashSet<>());

    public TileJobSystem(int numWorkers) {

        ExecutorService executor = null;
        try {
            executor = Executors.newFixedThreadPool(numWorkers);
            // this SonarQube rule can't be resolved because in the current version of Java
            // Executor cannot be auto-closed with try-with-resources
            for (int i = 0; i < numWorkers; i++) {
                executor.submit(this::worker);
            }
        }
        finally {
            assert executor != null;
            executor.shutdown();
        }
    }

    public void submitJob(TileJob job){
        processingJobs.add(job);
        queue.add(job);
    }

    private void processJob(TileJob job){
        try {

            BufferedImage image = CachedTileRepository.getInstance().getTileImageData(job.getTile());
            processingJobs.remove(job);
            job.getFuture().component2().complete(image);


        } catch (TileNotFoundException e) {
            processingJobs.remove(job);
            job.getFuture().component2().completeExceptionally(e);
        }
    }

    private void worker(){
        while (Thread.currentThread().isAlive()){
            try {
                TileJob job = queue.take();
                processJob(job);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}

package dataaccessobjects.tilejobs;

import dataaccessinterface.TileNotFoundException;
import dataaccessobjects.CachedTileRepository;

import java.awt.image.BufferedImage;
import java.util.concurrent.*;

public class TileJobSystem {
    private final ExecutorService executor;
    private final LinkedBlockingDeque<TileJob> queue = new LinkedBlockingDeque<>();

    public TileJobSystem(int numWorkers) {
        executor = Executors.newFixedThreadPool(numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            executor.submit(this::worker);
        }
    }

    public void submitJob(TileJob job){ queue.offer(job); }

    private void processJob(TileJob job){
        try {
            BufferedImage image = CachedTileRepository.getInstance().getTileImageData(job.getTile());
            job.getFuture().component2().complete(image);
        } catch (TileNotFoundException e) {
            job.getFuture().component2().completeExceptionally(e);
        }
    }

    private void worker(){
        while (true){
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

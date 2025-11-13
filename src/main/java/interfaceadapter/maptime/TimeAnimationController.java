package interfaceadapter.maptime;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/** Class that centrally modifies the current program time state
 *
 */
public class TimeAnimationController {
//    private final TimeAnimationInputBoundary timeAnimationUseCase;
    private ScheduledExecutorService scheduler;
    private final ProgramTimeController programTimeController;
    private volatile boolean playing;

    public TimeAnimationController(TimeAnimationInputBoundary timeAnimationInputBoundary,
                                   ProgramTimeController programTimeController) {
//        this.timeAnimationUseCase = timeAnimationInputBoundary;
        this.programTimeController = programTimeController;
        this.scheduler = Executors.newScheduledThreadPool(1);
        playing = false;
    }


    public synchronized void play() {
        if (playing){
            return;
        }
        playing = true;
        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
        }

        scheduler.scheduleAtFixedRate(() -> {
            try {
                tick();
            } catch (Exception e) {
                // TODO: log?
                pause();
            }
        }, 0, tickMillis, Time.Unit.Milliseconds);
    }


    private synchronized void pause(){
        playing = false;
        scheduler.shutdown();
    }

    private void tick(){
        return;
    }
}

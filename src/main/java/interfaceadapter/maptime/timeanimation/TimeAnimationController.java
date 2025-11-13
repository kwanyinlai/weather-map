package interfaceadapter.maptime.timeanimation;


import interfaceadapter.maptime.programtime.ProgramTimeController;

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
    private final int tickLength; // number of ms between ticks

    public TimeAnimationController(TimeAnimationInputBoundary timeAnimationInputBoundary,
                                   ProgramTimeController programTimeController,
                                   int tickLength
    ) {
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
        }, 0, tickLength, Time.Unit.Milliseconds);
    }


    private synchronized void pause(){
        playing = false;
        scheduler.shutdown();
    }

    private void tick(){
        programTimeController.updateTime(1);
    }
}

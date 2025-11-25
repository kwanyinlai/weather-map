package interfaceadapter.maptime.timeanimation;


import constants.Constants;
import usecase.maptime.TickMapTimeInputData;
import usecase.maptime.UpdateMapTimeInputBoundary;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Class that centrally modifies the current program time state
 *
 */
public class TimeAnimationController {
    private ScheduledExecutorService scheduler;
    private final UpdateMapTimeInputBoundary  updateMapTimeInputBoundary;
    private volatile boolean playing;
    private final int tickLength; // number of ms between ticks

    public TimeAnimationController(UpdateMapTimeInputBoundary updateMapTimeInputBoundary,
                                   int tickLength) {
        this.updateMapTimeInputBoundary = updateMapTimeInputBoundary;
        this.scheduler = Executors.newScheduledThreadPool(1);
        playing = false;
        this.tickLength = tickLength;
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
                pause();
            }
        }, Constants.ANIMATION_INITIAL_MS_DELAY, tickLength, TimeUnit.MILLISECONDS);
    }


    public synchronized void pause(){
        playing = false;
        scheduler.shutdownNow();
        scheduler = null;
    }

    private void tick(){
        updateMapTimeInputBoundary.execute(new TickMapTimeInputData(Constants.TICKS_PER_SECOND));
    }
}

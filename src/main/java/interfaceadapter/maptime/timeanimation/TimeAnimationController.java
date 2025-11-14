package interfaceadapter.maptime.timeanimation;


import interfaceadapter.maptime.programtime.ProgramTimeController;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Class that centrally modifies the current program time state
 *
 */
public class TimeAnimationController {
    private ScheduledExecutorService scheduler;
    private final ProgramTimeController programTimeController;
    private volatile boolean playing;
    private final int tickLength; // number of ms between ticks

    public TimeAnimationController(ProgramTimeController programTimeController,
                                   int tickLength) {
        this.programTimeController = programTimeController;
        this.scheduler = Executors.newScheduledThreadPool(1);
        playing = false;
        this.tickLength = tickLength;
    }

    public synchronized void play() {
        if (playing){
            return;
        }
        playing = true;
        System.out.println("Playing animation");
        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
        }
        scheduler.scheduleAtFixedRate(() -> {
            try {
                tick();
            } catch (Exception e) {
                pause();
            }
        }, 0, tickLength, TimeUnit.MILLISECONDS);
    }


    public synchronized void pause(){
        playing = false;
        System.out.println("Pausing animation");
        scheduler.shutdown();
    }

    private void tick(){
        programTimeController.updateTime(1);
        System.out.println("1 tick passed.");
    }
}

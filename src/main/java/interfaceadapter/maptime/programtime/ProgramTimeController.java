package interfaceadapter.maptime.programtime;

import usecase.maptime.UpdateMapTimeInputBoundary;
import usecase.maptime.UpdateMapTimeInputData;

/** Class that centrally modifies the current program time state
 *
 */
public class ProgramTimeController {
    private final UpdateMapTimeInputBoundary updateMapTimeUseCase;
    private final java.time.Duration maxForecast;

    public ProgramTimeController(UpdateMapTimeInputBoundary updateMapTimeInputBoundary, java.time.Duration maxForecast) {
        this.updateMapTimeUseCase = updateMapTimeInputBoundary;
        this.maxForecast = maxForecast;
    }

    // TODO: Add a timer to tick maybe every 10 minutes and force update time.

    /**
     *
     * @param programTimeState
     */
    public void updateTime(double sliderVal) {
        java.time.Instant maxTime = convertSliderToTime(sliderVal);
        updateMapTimeUseCase.execute(new UpdateMapTimeInputData(maxTime));
    }

    /** Increment by a fixed value
     *
     */
//    public void updateTime(int ticks){
//        // TODO: contain reset loop logic
//        TickMapTimeInputData in = new TickMapTimeInputData(1);
//        updateMapTimeUseCase.execute(in);
//    }

    /** Converts the slider value to a java.time.Instant
     *
     * @param scale the slider value from JSlider
     * @return the time represented on the slider
     */
    private java.time.Instant convertSliderToTime(double scale){
        java.time.Instant currentTime = java.time.Instant.now();
        return currentTime.plus(multiplyDuration(maxForecast, scale/100));
    }

    /** Multiply a duration by a scale factor
     *
     * @param duration duration to be multiplied
     * @param scale scale factor to multiply duration
     * @return duration * scale correct to the nearest minute
     */
    private java.time.Duration multiplyDuration(java.time.Duration duration, double scale) {
        return java.time.Duration.ofHours(Math.round(duration.toHours() * scale));
    }
}

package entity;

/** Class which represents the current time in the program. May not
 *  be the same as the current time, but the time on the slider.
 */
public class ProgramTime {

    /** the time which is represented */
    private java.time.Instant currentTime;
    /** the minimum interval in which program time can be incremented */
    private static java.time.Duration timeIncrement;

    public ProgramTime(java.time.Instant currentTime) {
        this.currentTime = currentTime;
    }

    /** Increment time by the minimum interval
     *
     */
    public void incrementTime(){
        currentTime = currentTime.plus(timeIncrement);
    }

    /** Increment time by amount of <code>duration</code>
     *
     * @param duration the duration to which program time should be incremented
     */
    public void incrementTime(java.time.Duration duration){
        currentTime = currentTime.plus(duration);
    }

    public void setTime(java.time.Instant currentTime) {
        this.currentTime = currentTime;
    }

    public java.time.Instant getCurrentTime() {
        return currentTime;
    }
}

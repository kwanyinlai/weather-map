package entity;

/** Class which represents the current time in the program. May not
 *  be the same as the current time, but the time on the slider.
 */
public class ProgramTime {

    /** the time which is represented. */
    private java.time.Instant currentTime;
    /** the minimum interval in which program time can be incremented. */
    public static final java.time.Duration TIME_INCREMENT = java.time.Duration.ofHours(1);
    private static final java.time.Duration MAX_FORECAST = java.time.Duration.ofDays(3);

    public ProgramTime(java.time.Instant currentTime) {
        this.currentTime = currentTime;
    }

    /** Increment time by the minimum interval.
     *
     */
    public void incrementTime(){
        currentTime = currentTime.plus(TIME_INCREMENT);
        if (currentTime.isAfter(java.time.Instant.now().plus(MAX_FORECAST))) {
            currentTime = java.time.Instant.now();
        }
    }

    /** Set the current program time to a specific time.
     * @param currentTime the time to set program time to
     */
    public void setTime(java.time.Instant currentTime) {
        this.currentTime = currentTime;
    }

    /** Get the current program time.
     * @return the current program time as a {@link java.time.Instant}
     */
    public java.time.Instant getCurrentTime() {
        return currentTime;
    }
}

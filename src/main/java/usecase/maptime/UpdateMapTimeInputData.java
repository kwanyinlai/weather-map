package usecase.maptime;

/** The input data for the current time for setting the time
 *  to a specific instant.
 */
public class UpdateMapTimeInputData {
    /** The current instant of time. */
    private final java.time.Instant currentTime;

    /** The current time constructor.
     * @param currentTime the current time.
     */
    public UpdateMapTimeInputData(java.time.Instant currentTime) {
        this.currentTime = currentTime;
    }
    /** Return the current time as a {@link java.time.Instant}.
     * @return the time
     */
    public java.time.Instant getCurrentTime() { return currentTime; }
}

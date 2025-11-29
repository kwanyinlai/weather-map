package usecase.maptime;
import java.time.Instant;

/** The output data for ProgramTime.
 */
public class UpdateMapTimeOutputData {
    private final Instant stamp;
    /** Constructor for OutputData.
     * @param stamp the time stamp
     */
    public UpdateMapTimeOutputData(Instant stamp) {
        this.stamp = stamp;
    }
    /** Return the current time as a time stamp.
     * @return the time as a {@link java.time.Instant}
     */
    public java.time.Instant getStamp() {
        return stamp;
    }
}

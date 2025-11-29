package usecase.maptime;

/** Represents the number of ticks that has passed by
 *  and thereby the number of ticks to increment.
 */
public class TickMapTimeInputData {
    /**
     * Number of ticks.
     */
    private final int ticks;
    /**
     * Creates an input data object for ticks
     * @param ticks number of ticks
     */
    public TickMapTimeInputData(int ticks) {
        if (ticks <= 0) {
            throw new IllegalArgumentException(
                    "Ticks must be non-negative integers."
            );
        }
        this.ticks = ticks;
    }
    /** Return the number of ticks.
     * @return number of ticks
     */
    public int getTicks() {
        return ticks;
    }
}

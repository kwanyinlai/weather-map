package usecase.maptime;

public interface UpdateMapTimeInputBoundary {
    /** Update the time to a set time.
     * @param updateMapTimeInputData the time to set to
     */
    void execute(UpdateMapTimeInputData updateMapTimeInputData);
    /** Update the time by a number of increments.
     * @param ticks the number of ticks that has passed
     */
    void execute(TickMapTimeInputData ticks);
}

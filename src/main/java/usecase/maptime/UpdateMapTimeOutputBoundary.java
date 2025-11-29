package usecase.maptime;

public interface UpdateMapTimeOutputBoundary {
    /** Pass new program time to the interactor indicating that
     * this is a discrete value from the slider.
     * @param time new program time
     */
    void updateTime(UpdateMapTimeOutputData time);

    /** Pass the new program time to the interactor, indicating that
     * this has come from the play/pause animator.
     * @param time
     */
    void updateTimeFromAnimator(UpdateMapTimeOutputData time);

    /** Increment the current time by one tick.
     */
    void incrementTime();

}

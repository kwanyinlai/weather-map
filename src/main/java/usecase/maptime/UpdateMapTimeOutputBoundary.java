package usecase.maptime;

public interface UpdateMapTimeOutputBoundary {
    /** Pass new program time to the interactor
     *
     * @param time new program time
     */
    void updateTime(UpdateMapTimeOutputData time);
    void incrementTime();

}

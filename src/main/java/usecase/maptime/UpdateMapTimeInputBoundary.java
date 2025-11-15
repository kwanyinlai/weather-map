package usecase.maptime;

public interface UpdateMapTimeInputBoundary {
    void execute(UpdateMapTimeInputData updateMapTimeInputData);
    void execute(TickMapTimeInputData ticks);
}

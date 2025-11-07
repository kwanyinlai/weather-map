package main.java.usecase.maptime;

public class UpdateMapTimeInputData {
    private final java.time.Instant currentTime;

    public UpdateMapTimeInputData(java.time.Instant currentTime) {
        this.currentTime = currentTime;
    }
    public java.time.Instant getCurrentTime() { return currentTime; }
}

package interface_adapter.maptime;

import usecase.maptime.UpdateMapTimeInputData;
import usecase.maptime.UpdateMapTimeUseCase;

public class ProgramTimeController {
    private final ProgramTimeState programTimeState;
    private final UpdateMapTimeUseCase updateMapTimeUseCase;
    private static final java.time.Duration maxForecast = java.time.Duration.ofHours(3);

    public ProgramTimeController(ProgramTimeState programTimeState, UpdateMapTimeUseCase updateMapTimeUseCase) {
        this.programTimeState = programTimeState;
        this.updateMapTimeUseCase = updateMapTimeUseCase;
    }

    public void execute(){
        java.time.Instant maxTime = convertSliderToTime();
        updateMapTimeUseCase.execute(new UpdateMapTimeInputData(maxTime));
    }

    public java.time.Instant convertSliderToTime(){
        double scale = programTimeState.getTimesliderScale(); // scale from 0 - 1
        java.time.Instant currentTime = java.time.Instant.now();
        return currentTime.plus(multiplyDuration(maxForecast, scale));
    }

    private java.time.Duration multiplyDuration(java.time.Duration duration, double scale) {
        return java.time.Duration.ofHours(Math.round(duration.toHours() * scale));
    }
}

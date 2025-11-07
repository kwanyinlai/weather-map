package interface_adapter.maptime;

import usecase.maptime.UpdateMapTimeInputData;
import usecase.maptime.UpdateMapTimeUseCase;

public class ProgramTimeController {
    private final UpdateMapTimeUseCase updateMapTimeUseCase;
    private static final java.time.Duration maxForecast = java.time.Duration.ofHours(3);

    public ProgramTimeController(UpdateMapTimeUseCase updateMapTimeUseCase) {
        this.updateMapTimeUseCase = updateMapTimeUseCase;
    }

    public void execute(ProgramTimeState programTimeState) {
        java.time.Instant maxTime = convertSliderToTime(programTimeState.getTimesliderScale());
        updateMapTimeUseCase.execute(new UpdateMapTimeInputData(maxTime));
    }

    public java.time.Instant convertSliderToTime(double scale){
        java.time.Instant currentTime = java.time.Instant.now();
        return currentTime.plus(multiplyDuration(maxForecast, scale));
    }

    private java.time.Duration multiplyDuration(java.time.Duration duration, double scale) {
        return java.time.Duration.ofHours(Math.round(duration.toHours() * scale));
    }
}

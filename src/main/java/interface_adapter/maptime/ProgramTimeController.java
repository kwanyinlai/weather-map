package interface_adapter.maptime;

import usecase.maptime.UpdateMapTimeInputData;
import usecase.maptime.UpdateMapTimeUseCase;

public class ProgramTimeController {
    private final UpdateMapTimeUseCase updateMapTimeUseCase;
    private final java.time.Duration maxForecast;

    public ProgramTimeController(UpdateMapTimeUseCase updateMapTimeUseCase, java.time.Duration maxForecast) {
        this.updateMapTimeUseCase = updateMapTimeUseCase;
        this.maxForecast = maxForecast;
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

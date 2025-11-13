package usecase.maptime;


import entity.ProgramTime;
import usecase.weatherLayers.update.UpdateOverlayUseCase;

public class UpdateMapTimeUseCase implements UpdateMapTimeInputBoundary {
    private final ProgramTime programTime;
    private final UpdateOverlayUseCase updateOverlayUseCase;
    private final UpdateMapTimeOutputBoundary programTimePresenter;

    public UpdateMapTimeUseCase(ProgramTime programTime, UpdateOverlayUseCase updateOverlayUseCase, UpdateMapTimeOutputBoundary programTimePresenter) {
        this.programTime = programTime;
        this.updateOverlayUseCase = updateOverlayUseCase;
        this.programTimePresenter = programTimePresenter;
    }

    @Override
    public void execute(UpdateMapTimeInputData updateMapTimeInputData) {
        programTime.setTime(updateMapTimeInputData.getCurrentTime());
        // updateOverlayUseCase.update();
        programTimePresenter.updateTime(
                new UpdateMapTimeOutputData(
                        updateMapTimeInputData.getCurrentTime()
                ));
    }

    @Override
    public void execute(TickMapTimeInputData ticks){
        programTime.incrementTime();
        // updateOverlayUseCase.update();
        programTimePresenter.updateTime(
                new UpdateMapTimeOutputData(
                        java.time.Instant.now().plus(ProgramTime.TIME_INCREMENT.multipliedBy(ticks.getTicks()))
                )
        );

    }
}

package usecase.maptime;


import entity.ProgramTime;
import usecase.weatherlayers.update.UpdateOverlayUseCase;

public class UpdateMapTimeUseCase implements UpdateMapTimeInputBoundary {
    private final ProgramTime programTime;
    private final UpdateOverlayInputBoundary updateOverlayUseCase;
    private final UpdateMapTimeOutputBoundary programTimePresenter;

    public UpdateMapTimeUseCase(ProgramTime programTime, UpdateOverlayInputBoundary updateOverlayUseCase, UpdateMapTimeOutputBoundary programTimePresenter) {
        this.programTime = programTime;
        this.updateOverlayUseCase = updateOverlayUseCase;
        this.programTimePresenter = programTimePresenter;
    }

    @Override
    public void execute(UpdateMapTimeInputData updateMapTimeInputData) {
        programTime.setTime(updateMapTimeInputData.getCurrentTime());
        updateOverlayUseCase.update();
        UpdateMapTimeOutputData outputData = new UpdateMapTimeOutputData(
                updateMapTimeInputData.getCurrentTime()
        );
        programTimePresenter.updateTime(
                outputData
                );
    }

    @Override
    public void execute(TickMapTimeInputData ticks){
        for(int i = 0; i < ticks.getTicks(); i++){
            programTime.incrementTime();
        }
        updateOverlayUseCase.update();

        UpdateMapTimeOutputData outputData = new UpdateMapTimeOutputData(
                programTime.getCurrentTime());

        programTimePresenter.updateTimeFromAnimator(
            outputData
        );

    }
}

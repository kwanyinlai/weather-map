package usecase.maptime;


import entity.ProgramTime;
import interface_adapter.maptime.ProgramTimePresenter;
import usecase.UpdateOverlayUseCase;
import usecase.maptime.UpdateMapTimeOutputData;
import usecase.maptime.UpdateMapTimeInputData;

public class UpdateMapTimeUseCase implements UpdateMapTimeInputBoundary {
    private final ProgramTime programTime;
    private final UpdateOverlayUseCase updateOverlayUseCase;
    private final ProgramTimePresenter programTimePresenter;

    public UpdateMapTimeUseCase(ProgramTime programTime, UpdateOverlayUseCase updateOverlayUseCase, ProgramTimePresenter programTimePresenter) {
        this.programTime = programTime;
        this.updateOverlayUseCase = updateOverlayUseCase;
        this.programTimePresenter = programTimePresenter;
    }

    @Override
    public void execute(UpdateMapTimeInputData updateMapTimeInputData) {
        programTime.setTime(updateMapTimeInputData.getCurrentTime());
//        updateOverlayUseCase.update();
        programTimePresenter.updateTime(new UpdateMapTimeOutputData(programTime.getCurrentTime()));
    }
}

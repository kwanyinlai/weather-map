package main.java.usecase.maptime;


import entity.ProgramTime;
import main.java.interfaceadapter.maptime.ProgramTimePresenter;
import usecase.UpdateOverlayUseCase;
import main.java.usecase.maptime.UpdateMapTimeInputBoundary;

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

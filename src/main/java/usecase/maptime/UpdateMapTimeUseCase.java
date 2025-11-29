package usecase.maptime;


import entity.ProgramTime;
import usecase.weatherlayers.updateoverlay.UpdateOverlayInputBoundary;

/** The use case class for UpdateMapTime. **/
public class UpdateMapTimeUseCase implements UpdateMapTimeInputBoundary {
    /** The program time entity. */
    private final ProgramTime programTime;
    /** the updating overlay use case to be called. */
    private final UpdateOverlayInputBoundary updateOverlayUseCase;
    /** Presenter for this use case. */
    private final UpdateMapTimeOutputBoundary programTimePresenter;

    /** Constructor for the UpdateMapTime usecase.
     * @param programTime the {@link ProgramTime} entity
     * @param updateOverlayUseCase the overlay usecase
     * @param programTimePresenter the presenter for this use case
     */
    public UpdateMapTimeUseCase(ProgramTime programTime,
                                UpdateOverlayInputBoundary updateOverlayUseCase,
                                UpdateMapTimeOutputBoundary programTimePresenter
    ) {
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
    public void execute(TickMapTimeInputData ticks) {
        for (int i = 0; i < ticks.getTicks(); i++) {
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

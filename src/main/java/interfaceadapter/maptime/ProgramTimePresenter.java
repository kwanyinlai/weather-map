package interfaceadapter.maptime;

import usecase.maptime.UpdateMapTimeOutputData;
import usecase.maptime.UpdateMapTimeOutputBoundary;

/** Presenter class for UpdateMapTime interactor
 *
 */
public class ProgramTimePresenter implements UpdateMapTimeOutputBoundary {
    private final ProgramTimeViewModel programTimeViewModel;

    public ProgramTimePresenter(ProgramTimeViewModel programTimeViewModel) {
        this.programTimeViewModel = programTimeViewModel;
    }

    @Override
    public void updateTime(UpdateMapTimeOutputData newTime) {
        // update program time in programTimeViewModel
        ProgramTimeState programTimeState = programTimeViewModel.getState();
        programTimeState.setTime(newTime.getStamp());
        programTimeViewModel.firePropertyChange("time slider");
    }
}

package interface_adapter.maptime;

import usecase.maptime.UpdateMapTimeInputData;
import usecase.maptime.UpdateMapTimeOutputData;
import interface_adapter.maptime.ProgramTimeViewModel;
import usecase.maptime.UpdateMapTimeOutputBoundary;

public class ProgramTimePresenter implements UpdateMapTimeOutputBoundary {
    private final ProgramTimeViewModel programTimeViewModel;

    public ProgramTimePresenter(ProgramTimeViewModel programTimeViewModel) {
        this.programTimeViewModel = programTimeViewModel;
    }

    @Override
    public void updateTime(UpdateMapTimeOutputData newTime) {
        // update program time in programTimeViewModel
        programTimeViewModel.firePropertyChange();
    }
}

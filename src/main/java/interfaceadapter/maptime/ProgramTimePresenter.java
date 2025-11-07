package main.java.interfaceadapter.maptime;

import main.java.usecase.maptime.UpdateMapTimeInputData;
import main.java.usecase.maptime.UpdateMapTimeOutputData;
import main.java.usecase.maptime.UpdateMapTimeOutputBoundary;

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

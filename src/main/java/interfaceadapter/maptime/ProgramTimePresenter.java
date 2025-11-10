package interfaceadapter.maptime;

import usecase.maptime.UpdateMapTimeOutputData;
import usecase.maptime.UpdateMapTimeOutputBoundary;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
        programTimeState.setTime(formatTimeInstant(newTime.getStamp()));
        programTimeViewModel.firePropertyChange("time slider");
    }
    private String formatTimeInstant(java.time.Instant instant) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }
}

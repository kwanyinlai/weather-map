package interfaceadapter.maptime;

import interface_adapter.ViewModel;
import interfaceadapter.maptime.ProgramTimeState;

public class ProgramTimeViewModel extends ViewModel<ProgramTimeState>{
    public static final String CURRENT_TIME_LABEL = "Current Time:";

    public ProgramTimeViewModel() {
        super("time slider");
        setState(new ProgramTimeState());
    }

}

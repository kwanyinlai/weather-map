package interfaceadapter.maptime;

import interfaceadapter.ViewModel;
import interfaceadapter.maptime.ProgramTimeState;

public class ProgramTimeViewModel extends ViewModel<ProgramTimeState>{

    public ProgramTimeViewModel() {
        super("time slider");
        setState(new ProgramTimeState());
    }

}

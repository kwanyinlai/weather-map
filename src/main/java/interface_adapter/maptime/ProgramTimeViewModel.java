package interface_adapter.maptime;
import interface_adapter.ViewModel;

public class ProgramTimeViewModel extends ViewModel<ProgramTimeState>{
    private final ProgramTimeViewModel programTimeViewModel;

    public ProgramTimeViewModel() {
        super("time slider");
        setState(new ProgramTimeState());
    }

}

package interfaceadapter.weatherlayers;

import interfaceadapter.ViewModel;



public class UpdateOverlayViewModel extends ViewModel<OverlayState> {

    public UpdateOverlayViewModel(){
        super("Overlay");
        setState(new OverlayState());
    }


}

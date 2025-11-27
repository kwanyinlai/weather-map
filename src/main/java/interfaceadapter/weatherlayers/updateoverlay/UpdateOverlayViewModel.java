package interfaceadapter.weatherlayers.updateoverlay;

import interfaceadapter.ViewModel;
import interfaceadapter.weatherlayers.layers.OverlayState;


public class UpdateOverlayViewModel extends ViewModel<OverlayState> {

    public UpdateOverlayViewModel(){
        super("Overlay");
        setState(new OverlayState());
    }


}

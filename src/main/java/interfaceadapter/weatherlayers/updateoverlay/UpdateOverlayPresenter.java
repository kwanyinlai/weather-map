package interfaceadapter.weatherlayers.updateoverlay;

import interfaceadapter.weatherlayers.layers.OverlayState;
import usecase.weatherlayers.updateoverlay.UpdateOverlayOutputBoundary;
import usecase.weatherlayers.updateoverlay.UpdateOverlayOutputData;

public class UpdateOverlayPresenter implements UpdateOverlayOutputBoundary {
    private final UpdateOverlayViewModel overlayViewModel;

    public UpdateOverlayPresenter(UpdateOverlayViewModel vm){
        overlayViewModel = vm;
    }

    @Override
    public void updateImage(UpdateOverlayOutputData data){
        OverlayState state = overlayViewModel.getState();
        state.setImage(data.getImage());
        overlayViewModel.firePropertyChange("overlay");
    }
}

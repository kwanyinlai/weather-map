package interfaceadapter.weatherLayers;

import usecase.weatherLayers.update.UpdateOverlayOutputBoundary;
import usecase.weatherLayers.update.UpdateOverlayOutputData;

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

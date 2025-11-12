package usecase.weatherLayers.update;

import entity.OverlayManager;

public class UpdateOverlaySizeUseCase implements UpdateOverlaySizeInputBoundary {
    private final OverlayManager overlayManager;

    public UpdateOverlaySizeUseCase(OverlayManager om){overlayManager = om;}

    public void changeSize(UpdateOverlaySizeInputData data){
        overlayManager.changeSize(data.getSize());
    }
}

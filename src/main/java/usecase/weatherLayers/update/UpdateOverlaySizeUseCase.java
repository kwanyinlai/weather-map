package usecase.weatherLayers.update;

import entity.OverlayManager;
import entity.Viewport;

public class UpdateOverlaySizeUseCase implements UpdateOverlaySizeInputBoundary {
    private final OverlayManager overlayManager;
    private final Viewport viewport;
    public UpdateOverlaySizeUseCase(OverlayManager om, Viewport vp){
        overlayManager = om;
        viewport = vp;
    }

    public void changeSize(UpdateOverlaySizeInputData data){
        viewport.setViewSize(data.getSize());
        overlayManager.changeSize(data.getSize());
    }
}

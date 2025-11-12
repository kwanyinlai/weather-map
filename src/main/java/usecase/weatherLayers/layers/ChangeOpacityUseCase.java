package usecase.weatherLayers.layers;

import entity.OverlayManager;

public class ChangeOpacityUseCase implements ChangeOpacityInputboundary{
    private final OverlayManager overlayManager;

    public ChangeOpacityUseCase(OverlayManager OM){
        this.overlayManager = OM;
    }

    @Override
    public void change(ChangeOpacityInputData data){
        this.overlayManager.setSelectedOpacity(data.getOpacity());
    }
}

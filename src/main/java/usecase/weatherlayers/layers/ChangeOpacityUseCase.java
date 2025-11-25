package usecase.weatherlayers.layers;

import entity.OverlayManager;

public class ChangeOpacityUseCase implements ChangeOpacityInputboundary{
    private final OverlayManager overlayManager;

    public ChangeOpacityUseCase(OverlayManager om){
        this.overlayManager = om;
    }

    @Override
    public void change(ChangeOpacityInputData data){
        this.overlayManager.setSelectedOpacity(Math.max(0, Math.min(1,data.getOpacity())));
    }
}

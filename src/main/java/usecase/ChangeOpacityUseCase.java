package usecase;

import entity.OverlayManager;

public class ChangeOpacityUseCase {
    private final OverlayManager overlayManager;

    public ChangeOpacityUseCase(OverlayManager OM){
        this.overlayManager = OM;
    }

    public void changeOpacity(double alpha){
        this.overlayManager.setSelectedOpacity((float)alpha);
    }
}

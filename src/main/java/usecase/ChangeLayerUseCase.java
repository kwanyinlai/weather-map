package usecase;

import entity.LayerNotFoundException;
import entity.OverlayManager;
import entity.WeatherType;

public class ChangeLayerUseCase {
    private final OverlayManager overlayManager;

    public ChangeLayerUseCase(OverlayManager OM){
        this.overlayManager = OM;
    }

    public void change(WeatherType type){
        try {
            this.overlayManager.setSelected(type);
            //output stuff,  set opacity slider
        } catch (LayerNotFoundException e) {
            //output stuff disable opacity slider
        }
    }
}


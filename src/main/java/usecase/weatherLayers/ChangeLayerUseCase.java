package usecase.weatherLayers;

import entity.LayerNotFoundException;
import entity.OverlayManager;
import entity.WeatherType;
import interfaceadapter.weatherLayers.WeatherLayersPresenter;

public class ChangeLayerUseCase implements ChangeLayerInputBoundary{
    private final OverlayManager overlayManager;
    private final ChangeLayerOutputBoundary layersPresenter;

    public ChangeLayerUseCase(OverlayManager OM, ChangeLayerOutputBoundary presenter){
        this.overlayManager = OM;
        this.layersPresenter = presenter;
    }

    @Override
    public void change(ChangeLayerInputData data) throws LayerNotFoundException{
            this.overlayManager.setSelected(data.getType());
            layersPresenter.updateOpacity(new ChangeLayersOutputData(overlayManager.getSelectedOpacity()));
    }
}


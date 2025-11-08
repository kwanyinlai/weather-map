package usecase.weatherLayers;

import entity.LayerNotFoundException;
import entity.OverlayManager;
import entity.WeatherType;
import interfaceadapter.weatherLayers.WeatherLayersPresenter;

public class ChangeLayerUseCase implements ChangeLayerInputBoundary{
    private final OverlayManager overlayManager;
    private final WeatherLayersPresenter layersPresenter;

    public ChangeLayerUseCase(OverlayManager OM, WeatherLayersPresenter presenter){
        this.overlayManager = OM;
        this.layersPresenter = presenter;
    }

    @Override
    public void change(ChangeLayerInputData data){
        try {
            this.overlayManager.setSelected(data.getType());
            layersPresenter.updateOpacity(new ChangeLayersOutputData(overlayManager.getSelectedOpacity()));
        } catch (LayerNotFoundException e) {
            layersPresenter.updateOpacity(new ChangeLayersOutputData(-1));
        }
    }
}


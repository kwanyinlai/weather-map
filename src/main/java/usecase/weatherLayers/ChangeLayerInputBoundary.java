package usecase.weatherLayers;

import entity.LayerNotFoundException;
import entity.WeatherType;

public interface ChangeLayerInputBoundary {
    void change(ChangeLayerInputData data) throws LayerNotFoundException;
}

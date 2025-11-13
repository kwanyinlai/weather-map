package usecase.weatherLayers.layers;

import entity.LayerNotFoundException;

public interface ChangeLayerInputBoundary {
    void change(ChangeLayerInputData data) throws LayerNotFoundException;
}

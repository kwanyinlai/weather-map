package usecase.weatherlayers.layers;

import entity.LayerNotFoundException;

public interface ChangeLayerInputBoundary {
    void change(ChangeLayerInputData data) throws LayerNotFoundException;
}

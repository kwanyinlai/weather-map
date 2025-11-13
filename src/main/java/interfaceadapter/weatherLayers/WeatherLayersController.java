package interfaceadapter.weatherLayers;

import entity.LayerNotFoundException;
import entity.WeatherType;
import usecase.weatherLayers.layers.ChangeLayerInputBoundary;
import usecase.weatherLayers.layers.ChangeLayerInputData;
import usecase.weatherLayers.layers.ChangeOpacityInputData;
import usecase.weatherLayers.layers.ChangeOpacityInputboundary;

public class WeatherLayersController {
    private final ChangeLayerInputBoundary layerInput;
    private final ChangeOpacityInputboundary opacityInput;

    public WeatherLayersController(ChangeLayerInputBoundary layerInput, ChangeOpacityInputboundary opacityInput){
        this.layerInput = layerInput;
        this.opacityInput = opacityInput;
    }

    public void executeChangeLayer(WeatherType type) throws LayerNotFoundException {
        layerInput.change(new ChangeLayerInputData(type));
    }

    public void executeChangeOpacity(int value){
        float alpha = (float) (value / 100.0);
        opacityInput.change(new ChangeOpacityInputData(alpha));
    }
}

package interfaceadapter.weatherlayers;

import entity.LayerNotFoundException;
import entity.WeatherType;
import usecase.weatherlayers.layers.ChangeLayerInputBoundary;
import usecase.weatherlayers.layers.ChangeLayerInputData;
import usecase.weatherlayers.layers.ChangeOpacityInputData;
import usecase.weatherlayers.layers.ChangeOpacityInputboundary;

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

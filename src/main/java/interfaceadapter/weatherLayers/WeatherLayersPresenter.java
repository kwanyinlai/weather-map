package interfaceadapter.weatherLayers;

import usecase.weatherLayers.layers.ChangeLayerOutputBoundary;
import usecase.weatherLayers.layers.ChangeLayersOutputData;

public class WeatherLayersPresenter implements ChangeLayerOutputBoundary {
    private final WeatherLayersViewModel vm;

    public WeatherLayersPresenter(WeatherLayersViewModel vm){
        this.vm = vm;
    }

    @Override
    public void updateOpacity(ChangeLayersOutputData data) {
        vm.setCurrentOpacity(data.getOpacity());
    }
}

package usecase.weatherLayers;

import entity.WeatherType;

public class ChangeLayerInputData {
    private final WeatherType type;

    public ChangeLayerInputData(WeatherType type){this.type = type;}

    public WeatherType getType(){return type;}

}

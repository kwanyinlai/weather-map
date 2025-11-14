package dataaccessinterface;

import entity.WeatherData;

public interface WeatherGateway {
    WeatherData fetchCurrentAndHourly(double lat, double lon) throws Exception;
}

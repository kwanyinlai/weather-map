package entity;

import java.util.List;
import java.util.Map;

public class WeatherData {
    public final String placeName;
    public final Map<String, Object> current;
    public final List<Map<String, Object>> hourly;

    public WeatherData(String placeName,
                       Map<String, Object> current,
                       List<Map<String, Object>> hourly) {
        this.placeName = placeName;
        this.current = current;
        this.hourly = hourly;
    }
}

package usecase.infopanel;

import entity.WeatherData;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class InfoPanelOutputData {
    public final String placeName;
    public final Double tempC;
    public final String condition;
    public final List<Double> hourlyTemps;
    public final Instant fetchedAt;
    public final Map<String, Object> current;
    public final List<Map<String, Object>> hourly;

    public InfoPanelOutputData(String placeName, Double tempC, String condition,
                               List<Double> hourlyTemps, Instant fetchedAt,
                               Map<String, Object> current, List<Map<String, Object>> hourly) {
        this.placeName = placeName;
        this.tempC = tempC;
        this.condition = condition;
        this.hourlyTemps = hourlyTemps == null ? Collections.emptyList() : hourlyTemps;
        this.fetchedAt = fetchedAt;
        this.current = current == null ? Collections.emptyMap() : current;
        this.hourly = hourly == null ? Collections.emptyList() : hourly;
    }

    public static InfoPanelOutputData fromWeatherData(WeatherData d, Instant at) {
        Double t = null; String c = null; List<Double> hs = Collections.emptyList();
        if (d.current != null) {
            Object tv = d.current.get("tempC");
            if (tv instanceof Number) t = ((Number) tv).doubleValue();
            Object cv = d.current.get("condition");
            if (cv instanceof String) c = (String) cv;
        }
        if (d.hourly != null) {
            hs = d.hourly.stream()
                    .map(m -> m.get("tempC"))
                    .filter(Objects::nonNull)
                    .map(Number.class::cast)
                    .map(Number::doubleValue)
                    .collect(Collectors.toList());
        }
        return new InfoPanelOutputData(d.placeName, t, c, hs, at, d.current, d.hourly);
    }
}

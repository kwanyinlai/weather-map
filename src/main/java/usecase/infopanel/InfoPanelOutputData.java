package usecase.infopanel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class InfoPanelOutputData {
    public final String placeName;
    public final Double tempC;
    public final String condition;
    public final List<Double> hourlyTemps;
    public final Instant fetchedAt;
    public final Map<String, String> extra;

    public InfoPanelOutputData(String placeName, Double tempC, String condition,
                               List<Double> hourlyTemps, Instant fetchedAt, Map<String, String> extra) {
        this.placeName = placeName;
        this.tempC = tempC;
        this.condition = condition;
        this.hourlyTemps = hourlyTemps;
        this.fetchedAt = fetchedAt;
        this.extra = extra;
    }
}

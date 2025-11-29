package usecase.infopanel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class InfoPanelOutputData {
    private final String placeName;
    private final Double tempC;
    private final String condition;
    private final List<Double> hourlyTemps;
    private final Instant fetchedAt;
    private final Map<String, String> extra;

    public InfoPanelOutputData(String placeName, Double tempC, String condition,
                               List<Double> hourlyTemps, Instant fetchedAt, Map<String, String> extra) {
        this.placeName = placeName;
        this.tempC = tempC;
        this.condition = condition;
        this.hourlyTemps = hourlyTemps;
        this.fetchedAt = fetchedAt;
        this.extra = extra;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Double getTempC() {
        return tempC;
    }

    public String getCondition() {
        return condition;
    }

    public List<Double> getHourlyTemps() {
        return hourlyTemps;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }

    public Map<String, String> getExtra() {
        return extra;
    }
}

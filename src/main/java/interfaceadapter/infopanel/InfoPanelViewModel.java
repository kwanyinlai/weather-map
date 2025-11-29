package interfaceadapter.infopanel;

import static constants.Constants.*;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import usecase.infopanel.InfoPanelError;

public class InfoPanelViewModel {
    public static final int MARGIN = INFO_PANEL_MARGIN;

    public static final int PAD = INFO_PANEL_PAD;
    public static final int RADIUS = INFO_PANEL_RADIUS;

    public static final Color BG = INFO_PANEL_BG_COLOR;
    public static final Color STROKE = INFO_PANEL_STROKE_COLOR;
    public static final Color SUBTLE = INFO_PANEL_SUBTLE_COLOR;
    public static final Color CLOSE_BG = INFO_PANEL_CLOSE_BG_COLOR;
    public static final Color CLOSE_BG_HOVER = INFO_PANEL_CLOSE_BG_HOVER;

    public static final Font F_HEADER = new Font(FONT_NAME, Font.BOLD, INFO_PANEL_HEADER_SIZE);
    public static final Font F_CITY = new Font(FONT_NAME, Font.BOLD, INFO_PANEL_CITY_SIZE);
    public static final Font F_BIG = new Font(FONT_NAME, Font.PLAIN, INFO_PANEL_BIG_SIZE);
    public static final Font F_BODY = new Font(FONT_NAME, Font.PLAIN, INFO_PANEL_BODY_SIZE);

    public static final DateTimeFormatter HOUR_FMT =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());

    // XML tag names
    public static final String TAG_LOCATION = "location";
    public static final String TAG_NAME = "name";
    public static final String TAG_CURRENT = "current";
    public static final String TAG_CONDITION = "condition";
    public static final String TAG_CONDITION_TEXT = "text";
    public static final String TAG_TEMP_C = "temp_c";
    public static final String TAG_FORECAST_DAY = "forecastday";
    public static final String TAG_HOUR = "hour";

    // extra map keys
    public static final String EXTRA_HUMIDITY = "humidity";
    public static final String EXTRA_WIND_KPH = "wind_kph";
    public static final String EXTRA_WIND_DIR = "wind_dir";
    public static final String EXTRA_PRESSURE_MB = "pressure_mb";
    public static final String EXTRA_PRECIP_MM = "precip_mm";
    public static final String EXTRA_FEELSLIKE_C = "feelslike_c";
    public static final String EXTRA_UV = "uv";
    public static final String EXTRA_CLOUD = "cloud";

    private boolean visible;
    private boolean loading;
    private InfoPanelError error;
    private String placeName;
    private Double tempC;
    private String condition;
    private List<Double> hourlyTemps;
    private Instant fetchedAt;

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public InfoPanelError getError() {
        return error;

    }

    public void setError(InfoPanelError error) {
        this.error = error;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Double getTempC() {
        return tempC;
    }

    public void setTempC(Double tempC) {
        this.tempC = tempC;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<Double> getHourlyTemps() {
        return hourlyTemps;
    }

    public void setHourlyTemps(List<Double> hourlyTemps) {
        this.hourlyTemps = hourlyTemps;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(Instant fetchedAt) {
        this.fetchedAt = fetchedAt;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}

package interfaceadapter.infopanel;

import usecase.infopanel.InfoPanelError;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static constants.Constants.*;

public class InfoPanelViewModel {
    public boolean visible = false;
    public boolean loading;
    public InfoPanelError error;
    public String placeName;
    public Double tempC;
    public String condition;
    public List<Double> hourlyTemps;
    public Instant fetchedAt;

    public static final int MARGIN = INFO_PANEL_MARGIN;

    public static final int PAD = INFO_PANEL_PAD;
    public static final int RADIUS = INFO_PANEL_RADIUS;

    public static final Color BG = INFO_PANEL_BG_COLOR;
    public static final Color STROKE = INFO_PANEL_STROKE_COLOR;
    public static final Color SUBTLE = INFO_PANEL_SUBTLE_COLOR;
    public static final Color CLOSE_BG = INFO_PANEL_CLOSE_BG_COLOR;
    public static final Color CLOSE_BG_HOVER = INFO_PANEL_CLOSE_BG_HOVER;
    public static final Font F_HEADER = new Font("SansSerif", Font.BOLD, INFO_PANEL_HEADER_SIZE);
    public static final Font F_CITY   = new Font("SansSerif", Font.BOLD, INFO_PANEL_CITY_SIZE);
    public static final Font F_BIG    = new Font("SansSerif", Font.PLAIN, INFO_PANEL_BIG_SIZE);
    public static final Font F_BODY   = new Font("SansSerif", Font.PLAIN, INFO_PANEL_BODY_SIZE);

    public static final DateTimeFormatter HOUR_FMT =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());
}

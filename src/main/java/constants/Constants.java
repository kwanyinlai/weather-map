package constants;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    public static final int DEFAULT_PROGRAM_WIDTH = 800;
    public static final int DEFAULT_PROGRAM_HEIGHT = 800;
    public static final int DEFAULT_MAP_WIDTH = 600;
    public static final int DEFAULT_MAP_HEIGHT = 600;
    public static final Path BOOKMARK_DATA_PATH = Paths.get("data/bookmarks.json");

    public static final int ZOOM_THRESHOLD = 8;

    public static final int INFO_PANEL_MARGIN = 15;
    public static final int INFO_PANEL_WIDTH = 300;
    public static final int INFO_PANEL_HEIGHT = 370;
    public static final int INFO_PANEL_PAD = 14;
    public static final int INFO_PANEL_RADIUS = 16;

    public static final Color INFO_PANEL_BG_COLOR = new Color(255,255,255,235);
    public static final Color INFO_PANEL_STROKE_COLOR = new Color(30,30,30);
    public static final Color INFO_PANEL_SUBTLE_COLOR = new Color(0,0,0,110);
    public static final Color INFO_PANEL_CLOSE_BG_COLOR = new Color(0,0,0,30);
    public static final Color INFO_PANEL_CLOSE_BG_HOVER = new Color(0,0,0,60);

    public static final int INFO_PANEL_HEADER_SIZE = 20;
    public static final int INFO_PANEL_CITY_SIZE = 26;
    public static final int INFO_PANEL_BIG_SIZE = 18;
    public static final int INFO_PANEL_BODY_SIZE = 14;

    public static final long INFO_PANEL_SECONDS_TO_ADD = 3600L;

    public Constants(){}
}

package constants;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constants {
    public static final int DEFAULT_PROGRAM_WIDTH = 800;
    public static final int DEFAULT_PROGRAM_HEIGHT = 800;
    public static final int DEFAULT_MAP_WIDTH = 600;
    public static final int DEFAULT_MAP_HEIGHT = 600;
    public static final int API_MAX_DAY_LIMIT = 3;
    public static final int HOURS_PER_DAY = 24;
    public static final int ANIMATION_INITIAL_MS_DELAY = 300;
    public static final int TICKS_PER_SECOND = 1;
    public static final int PLAY_PAUSE_BUTTON_HEIGHT = 20;
    public static final int PLAY_PAUSE_BUTTON_WIDTH = 20;
    public static final int SLIDER_WIDTH = 400;
    public static final int SLIDER_HEIGHT = 50;
    public static final int CACHE_SIZE = 200;
    public static final java.time.Duration API_MAX_DAY_LIMIT_DURATION =
            java.time.Duration.ofDays(3);
    public static final int TICK_LENGTH_MS = 250;
    public static final Path BOOKMARK_DATA_PATH = Paths.get("data/bookmarks.json");

    public static final int ZOOM_THRESHOLD = 8;

    public static final int INFO_PANEL_MARGIN = 15;
    public static final int INFO_PANEL_WIDTH = 300;
    public static final int INFO_PANEL_HEIGHT = 370;
    public static final int INFO_PANEL_PAD = 14;
    public static final int INFO_PANEL_RADIUS = 16;

    public static final Color INFO_PANEL_BG_COLOR = new Color(255, 255, 255, 235);
    public static final Color INFO_PANEL_STROKE_COLOR = new Color(30, 30, 30);
    public static final Color INFO_PANEL_SUBTLE_COLOR = new Color(0, 0, 0, 110);
    public static final Color INFO_PANEL_CLOSE_BG_COLOR = new Color(0, 0, 0, 30);
    public static final Color INFO_PANEL_CLOSE_BG_HOVER = new Color(0, 0, 0, 60);
    public static final String FONT_NAME = "SansSerif";

    public static final int INFO_PANEL_HEADER_SIZE = 20;
    public static final int INFO_PANEL_CITY_SIZE = 26;
    public static final int INFO_PANEL_BIG_SIZE = 18;
    public static final int INFO_PANEL_BODY_SIZE = 14;
    public static final int ARC = 10;
    public static final int INFO_PANEL_DIVIDER_GAP = 16;
    public static final int INFO_PANEL_LABEL_GAP = 6;
    public static final int INFO_PANEL_TIME_GAP = 12;
    public static final int INFO_PANEL_HOURLY_HEADER_GAP = 8;
    public static final int HOURLY_VALUE_COL_OFFSET = 120;
    public static final int HOURLY_ROW_EXTRA_PADDING = 4;
    public static final int HOURLY_MAX_ROWS = 10;
    public static final int HOURLY_TIME_X = 14;
    public static final int HOURLY_VALUE_X = 134;

    public static final int CLOSE_BUTTON_SIZE = 28;
    public static final int CLOSE_BUTTON_TOP_OFFSET = 55;

    public static final long INFO_PANEL_SECONDS_TO_ADD = 3600L;
    public static final Path MAP_SETTINGS_DATA_PATH = Paths.get("data/map-settings.json");
    public static final int TILE_FETCH_WORKERS = 8;
    public static final float DEFAULT_OPACITY = (float) 0.5;
    public static final int MAX_WEATHERTILE_ZOOM = 6;
    public static final int SEARCH_BAR_PRFFERDSIZE_WIDTH = 10;
    public static final int SEARCH_BAR_PRFFERDSIZE_HEIGHT = 10;
    public static final int RESULTSCROLL_SIZE_WIDTH = 250;
    public static final int RESULTSCROLL_SIZE_HEIGHT = 100;
    public static final int NUM_VISIBLE_SEARCH_RESULTS = 3;
    public static final int AFTER_SEARCHING_ZOOM_LEVEL = 10;
    public static final int SEARCH_PANEL_HEIGHT = 60;
    public static final int SEARCH_PANEL_WIDTH = 250;
    public static final int WEATHERTILE_SIZE = 256;
    public static final int ZOOM_BAILOUT_OFFSET = 4;
    public static final double ZOOM_MAP_TO_WEATHER_FACTOR = 1.5;
    public static final int CHANGE_WEATHER_VIEW_SIZE = 200;
    public static final int OPACITY_SLIDER_DEFAULT_VAL = 50;
    public static final int OPACITY_SLIDER_MAX_VAL = 100;
    public static final int OVERLAY_ALIGNMENT_OFFSET = 5;
    public static final int CHANGESIZE_CALL_DELAY = 15;
    public static final int PERCENT_MULTIPLIER = 100;
    public static final String PLAY_BUTTON_FILE_PATH = "img/play.png";
    public static final String PAUSE_BUTTON_FILE_PATH = "img/pause.png";

    private Constants() { // intentionally empty
    }
}

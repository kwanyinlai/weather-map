package usecase.mapsettings.savemapsettings;

import entity.WeatherType;

/**
 * Input data for the "save map settings" use case.
 * Contains the information needed to persist the current map state.
 */
public final class SaveMapSettingsInputData {

    private final double centerLatitude;
    private final double centerLongitude;
    private final int zoomLevel;
    private final WeatherType weatherType;

    /**
     * Constructs input data for saving map settings.
     *
     * @param centerLatitude  latitude of the current map center
     * @param centerLongitude longitude of the current map center
     * @param zoomLevel       current zoom level of the map
     * @param weatherType     currently selected weather type (may be null)
     */
    public SaveMapSettingsInputData(double centerLatitude,
                                    double centerLongitude,
                                    int zoomLevel,
                                    WeatherType weatherType) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.zoomLevel = zoomLevel;
        this.weatherType = weatherType;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }
}

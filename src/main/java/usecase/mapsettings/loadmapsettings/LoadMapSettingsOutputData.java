package usecase.mapsettings.loadmapsettings;

import entity.WeatherType;

/**
 * Data returned by the "load map settings" use case to the presenter.
 */
public final class LoadMapSettingsOutputData {

    private final double centerLatitude;
    private final double centerLongitude;
    private final int zoomLevel;
    private final WeatherType weatherType;

    /**
     * Constructs output data representing the saved map state.
     *
     * @param centerLatitude  latitude of the last saved map center
     * @param centerLongitude longitude of the last saved map center
     * @param zoomLevel       last saved zoom level
     * @param weatherType     last saved weather type (may be null)
     */
    public LoadMapSettingsOutputData(double centerLatitude,
                                     double centerLongitude,
                                     int zoomLevel,
                                     WeatherType weatherType) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.zoomLevel = zoomLevel;
        this.weatherType = weatherType;
    }

    /**
     * Returns the latitude of the last saved map center.
     */
    public double getCenterLatitude() {
        return centerLatitude;
    }

    /**
     * Returns the longitude of the last saved map center.
     */
    public double getCenterLongitude() {
        return centerLongitude;
    }

    /**
     * Returns the last saved zoom level.
     */
    public int getZoomLevel() {
        return zoomLevel;
    }

    /**
     * Returns the last saved weather type.
     */
    public WeatherType getWeatherType() {
        return weatherType;
    }
}

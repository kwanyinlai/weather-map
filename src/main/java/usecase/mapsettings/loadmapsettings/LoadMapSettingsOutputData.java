package usecase.mapsettings.loadmapsettings;

/**
 * Data returned by the "load map settings" use case to the presenter.
 */
public final class LoadMapSettingsOutputData {

    private final double centerLatitude;
    private final double centerLongitude;
    private final int zoomLevel;

    /**
     * Constructs output data representing the saved map state.
     *
     * @param centerLatitude  latitude of the last saved map center
     * @param centerLongitude longitude of the last saved map center
     * @param zoomLevel       last saved zoom level
     */
    public LoadMapSettingsOutputData(double centerLatitude,
                                     double centerLongitude,
                                     int zoomLevel) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.zoomLevel = zoomLevel;
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
}

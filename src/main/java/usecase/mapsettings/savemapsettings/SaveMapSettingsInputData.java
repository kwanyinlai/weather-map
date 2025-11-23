package usecase.mapsettings.savemapsettings;

/**
 * Input data for the "save map settings" use case.
 * Contains the information needed to persist the current map state.
 */
public final class SaveMapSettingsInputData {

    private final double centerLatitude;
    private final double centerLongitude;
    private final int zoomLevel;

    /**
     * Constructs input data for saving map settings.
     *
     * @param centerLatitude  latitude of the current map center
     * @param centerLongitude longitude of the current map center
     * @param zoomLevel       current zoom level of the map
     */
    public SaveMapSettingsInputData(double centerLatitude,
                                    double centerLongitude,
                                    int zoomLevel) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.zoomLevel = zoomLevel;
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
}

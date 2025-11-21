package interfaceadapter.mapsettings;

import interfaceadapter.ViewModel;

/**
 * ViewModel for the map settings screen.
 *
 * <p>This class extends the generic {@link ViewModel} and specializes it
 * for holding the current / last-saved map center and zoom, plus simple
 * status information such as whether settings exist and any error message.</p>
 */
public final class MapSettingsViewModel
        extends ViewModel<MapSettingsViewModel.MapSettingsState> {

    /**
     * Logical name for this view (useful if you switch between multiple views).
     */
    public static final String VIEW_NAME = "mapSettings";

    /**
     * Property name used when firing state change events.
     */
    public static final String STATE_PROPERTY = "state";

    /**
     * Creates a MapSettingsViewModel with a default initial state.
     * By default, we assume there are no saved settings yet.
     */
    public MapSettingsViewModel() {
        super(VIEW_NAME);
        // Default: no saved settings, dummy coordinates/zoom, no error.
        setState(new MapSettingsState(
                false,   // hasSavedSettings
                0.0,     // centerLatitude
                0.0,     // centerLongitude
                1,       // zoomLevel
                null     // errorMessage
        ));
    }

    /**
     * Updates the map settings in the state and notifies listeners.
     *
     * <p>Typical use: called by the presenter when settings are successfully
     * loaded from disk, or after the user changes the map and the app
     * wishes to reflect that in the UI.</p>
     *
     * @param centerLatitude   latitude of the map center
     * @param centerLongitude  longitude of the map center
     * @param zoomLevel        zoom level
     * @param hasSavedSettings whether these settings come from a saved state
     *                         (true) or are just defaults (false)
     */
    public void setMapSettings(double centerLatitude,
                               double centerLongitude,
                               int zoomLevel,
                               boolean hasSavedSettings) {

        MapSettingsState current = getState();
        String currentError = current == null ? null : current.getErrorMessage();

        MapSettingsState newState = new MapSettingsState(
                hasSavedSettings,
                centerLatitude,
                centerLongitude,
                zoomLevel,
                currentError
        );

        setState(newState);
        firePropertyChange(STATE_PROPERTY);
    }

    /**
     * Updates only the error message in the state and notifies listeners.
     *
     * <p>Typical use: called by the presenter when loading/saving settings
     * fails for some reason.</p>
     *
     * @param errorMessage the new error message, or {@code null} to clear it
     */
    public void setErrorMessage(String errorMessage) {
        MapSettingsState current = getState();

        boolean hasSavedSettings =
                current != null && current.hasSavedSettings();
        double lat =
                current != null ? current.getCenterLatitude() : 0.0;
        double lon =
                current != null ? current.getCenterLongitude() : 0.0;
        int zoom =
                current != null ? current.getZoomLevel() : 1;

        MapSettingsState newState = new MapSettingsState(
                hasSavedSettings,
                lat,
                lon,
                zoom,
                errorMessage
        );

        setState(newState);
        firePropertyChange(STATE_PROPERTY);
    }

    /**
     * Immutable state object representing everything the map settings view
     * needs to render itself.
     */
    public static final class MapSettingsState {

        private final boolean hasSavedSettings;
        private final double centerLatitude;
        private final double centerLongitude;
        private final int zoomLevel;
        private final String errorMessage;

        /**
         * Creates a new map settings state.
         *
         * @param hasSavedSettings whether settings have been successfully loaded/saved before
         * @param centerLatitude   latitude of the map center
         * @param centerLongitude  longitude of the map center
         * @param zoomLevel        zoom level
         * @param errorMessage     optional error message, or null if no error
         */
        public MapSettingsState(boolean hasSavedSettings,
                                double centerLatitude,
                                double centerLongitude,
                                int zoomLevel,
                                String errorMessage) {
            this.hasSavedSettings = hasSavedSettings;
            this.centerLatitude = centerLatitude;
            this.centerLongitude = centerLongitude;
            this.zoomLevel = zoomLevel;
            this.errorMessage = errorMessage;
        }

        /**
         * Returns whether valid saved settings exist.
         */
        public boolean hasSavedSettings() {
            return hasSavedSettings;
        }

        /**
         * Returns the center latitude.
         */
        public double getCenterLatitude() {
            return centerLatitude;
        }

        /**
         * Returns the center longitude.
         */
        public double getCenterLongitude() {
            return centerLongitude;
        }

        /**
         * Returns the zoom level.
         */
        public int getZoomLevel() {
            return zoomLevel;
        }

        /**
         * Returns the current error message, or null if none.
         */
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}

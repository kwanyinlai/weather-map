package interfaceadapter.mapsettings;

import interfaceadapter.ViewModel;

/**
 * ViewModel for the map settings screen.
 */
public final class MapSettingsViewModel
        extends ViewModel<MapSettingsViewModel.MapSettingsState> {

    public static final String VIEW_NAME = "map_settings";

    /**
     * Property name used when firing state change events.
     */
    public static final String STATE_PROPERTY = "state";

    /**
     * Constructs a new MapSettingsViewModel with an initial "no settings" state.
     */
    public MapSettingsViewModel() {
        super(VIEW_NAME);
        this.setState(new MapSettingsState(false, 0.0, 0.0, 0, null));
    }

    @Override
    public void setState(MapSettingsState state) {
        super.setState(state);
        firePropertyChange(STATE_PROPERTY);
    }

    /**
     * Update the state when valid settings are loaded/saved successfully.
     */
    public void setSettings(double centerLatitude,
                            double centerLongitude,
                            int zoomLevel) {
        MapSettingsState newState = new MapSettingsState(
                true,
                centerLatitude,
                centerLongitude,
                zoomLevel,
                null
        );
        setState(newState);
    }

    /**
     * Update the state when an error occurs (e.g., load failure).
     * Keeps any existing settings but updates the error message.
     */
    public void setError(String errorMessage) {
        MapSettingsState current = getState();
        if (current == null) {
            current = new MapSettingsState(false, 0.0, 0.0, 0, errorMessage);
        } else {
            current = new MapSettingsState(
                    current.hasSavedSettings(),
                    current.getCenterLatitude(),
                    current.getCenterLongitude(),
                    current.getZoomLevel(),
                    errorMessage
            );
        }
        setState(current);
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
         * @param hasSavedSettings whether valid settings exist
         * @param centerLatitude   map center latitude
         * @param centerLongitude  map center longitude
         * @param zoomLevel        zoom level
         * @param errorMessage     optional error message, or null if none
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

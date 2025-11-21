package interfaceadapter.mapsettings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View model for map settings (saved center and zoom).
 *
 * <p>This class sits in the interface-adapter layer and holds the state that
 * map-related views care about:
 * <ul>
 *     <li>Saved center latitude/longitude</li>
 *     <li>Saved zoom level</li>
 *     <li>Whether any saved settings exist</li>
 *     <li>A user-facing error message (if any)</li>
 * </ul>
 *
 * <p>Presenters write into this view model, and Swing views listen for
 * property change events and redraw themselves when the state changes.</p>
 */
public class MapSettingsViewModel {

    /**
     * Property name fired when the saved map settings (center/zoom/exists)
     * change.
     */
    public static final String MAP_SETTINGS_PROPERTY = "mapSettings";

    /**
     * Property name fired when the error message changes.
     */
    public static final String ERROR_PROPERTY = "errorMessage";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // Saved settings state.
    private Double centerLatitude;
    private Double centerLongitude;
    private Integer zoomLevel;
    private boolean hasSavedSettings;

    // Error state.
    private String errorMessage;

    /**
     * Immutable snapshot of the map settings portion of the view model.
     * Used as the "old" and "new" values in MAP_SETTINGS_PROPERTY events.
     */
    public static final class MapSettingsState {
        public final Double centerLatitude;
        public final Double centerLongitude;
        public final Integer zoomLevel;
        public final boolean hasSavedSettings;

        public MapSettingsState(Double centerLatitude,
                                Double centerLongitude,
                                Integer zoomLevel,
                                boolean hasSavedSettings) {
            this.centerLatitude = centerLatitude;
            this.centerLongitude = centerLongitude;
            this.zoomLevel = zoomLevel;
            this.hasSavedSettings = hasSavedSettings;
        }
    }

    /**
     * Creates an immutable snapshot of the current map settings state.
     */
    private MapSettingsState snapshot() {
        return new MapSettingsState(centerLatitude, centerLongitude, zoomLevel, hasSavedSettings);
    }

    // ===== Getters used by the view =====

    /**
     * @return latitude of the saved map center, or {@code null} if none.
     */
    public Double getCenterLatitude() {
        return centerLatitude;
    }

    /**
     * @return longitude of the saved map center, or {@code null} if none.
     */
    public Double getCenterLongitude() {
        return centerLongitude;
    }

    /**
     * @return saved zoom level, or {@code null} if none.
     */
    public Integer getZoomLevel() {
        return zoomLevel;
    }

    /**
     * @return true if saved settings exist, false otherwise.
     */
    public boolean hasSavedSettings() {
        return hasSavedSettings;
    }

    /**
     * @return current error message, or {@code null} if there is no error.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    // ===== Mutations used by presenters =====

    /**
     * Sets the saved map settings (center + zoom) and marks that saved
     * settings exist.
     *
     * <p>Typical caller: {@code LoadMapSettingsPresenter.presentLoadedSettings(...)}.</p>
     *
     * @param latitude  latitude of saved map center
     * @param longitude longitude of saved map center
     * @param zoomLevel saved zoom level
     */
    public void setMapSettings(double latitude, double longitude, int zoomLevel) {
        MapSettingsState oldState = snapshot();

        this.centerLatitude = latitude;
        this.centerLongitude = longitude;
        this.zoomLevel = zoomLevel;
        this.hasSavedSettings = true;

        MapSettingsState newState = snapshot();
        support.firePropertyChange(MAP_SETTINGS_PROPERTY, oldState, newState);
    }

    /**
     * Clears any saved settings and marks that none are available.
     *
     * <p>Typical caller: {@code LoadMapSettingsPresenter.presentNoSavedSettings()}.</p>
     */
    public void clearSettings() {
        MapSettingsState oldState = snapshot();

        this.centerLatitude = null;
        this.centerLongitude = null;
        this.zoomLevel = null;
        this.hasSavedSettings = false;

        MapSettingsState newState = snapshot();
        support.firePropertyChange(MAP_SETTINGS_PROPERTY, oldState, newState);
    }

    /**
     * Updates the current error message and notifies listeners.
     *
     * <p>Typical callers:
     * <ul>
     *     <li>{@code LoadMapSettingsPresenter.presentLoadSettingsFailure(...)} </li>
     *     <li>{@code SaveMapSettingsPresenter.presentSaveSettingsFailure(...)} </li>
     *     <li>{@code SaveMapSettingsPresenter.presentSavedSettings(...)} (to clear errors)</li>
     * </ul>
     * </p>
     *
     * @param newErrorMessage new error message, or {@code null} to clear
     */
    public void setErrorMessage(String newErrorMessage) {
        String old = this.errorMessage;
        this.errorMessage = newErrorMessage;
        support.firePropertyChange(ERROR_PROPERTY, old, newErrorMessage);
    }

    // ===== Listener management =====

    /**
     * Registers a listener for property change events.
     *
     * @param listener listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Unregisters a previously registered property change listener.
     *
     * @param listener listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}

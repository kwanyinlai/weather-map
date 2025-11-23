package dataaccessinterface;

import entity.Location;
import entity.WeatherType;

/**
 * Interface for persisting and retrieving the user's last map settings
 * (e.g., center location, zoom level, and selected weather type) when the app runs.
 */
public interface SavedMapOverlaySettings {

    /**
     * Returns whether there is a previously persisted map state.
     *
     * @return {@code true} if map settings have been saved before;
     *         {@code false} otherwise.
     */
    boolean hasSavedSettings();

    /**
     * Returns the last saved center location of the map.
     *
     * <p>Callers should first check {@link #hasSavedSettings()} to ensure
     * a value is available.</p>
     *
     * @return the last saved center {@link Location}.
     */
    Location getSavedCenterLocation();

    /**
     * Returns the last saved zoom level of the map.
     *
     * <p>Callers should first check {@link #hasSavedSettings()} to ensure
     * a value is available.</p>
     *
     * @return the last saved zoom level.
     */
    int getSavedZoomLevel();

    /**
     * Returns the last saved weather type.
     *
     * <p>Callers should first check {@link #hasSavedSettings()} to ensure
     * a value is available.</p>
     *
     * @return the last saved weather type, or null if not saved.
     */
    WeatherType getSavedWeatherType();

    /**
     * Persists the current map state.
     *
     * @param centerLocation the current center of the map with coordinates as in {@link Location}.
     * @param zoomLevel      the current zoom level of the map.
     * @param weatherType    the currently selected weather type.
     */
    void save(Location centerLocation, int zoomLevel, WeatherType weatherType);

}

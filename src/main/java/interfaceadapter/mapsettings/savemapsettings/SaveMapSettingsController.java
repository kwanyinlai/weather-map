package interfaceadapter.mapsettings.savemapsettings;

import usecase.mapsettings.savemapsettings.SaveMapSettingsInputBoundary;
import usecase.mapsettings.savemapsettings.SaveMapSettingsInputData;

/**
 * Controller for the "save map settings" use case.
 *
 * <p>Called by the UI when the user changes the map view and the new
 * state should be persisted.</p>
 */
public final class SaveMapSettingsController {

    private final SaveMapSettingsInputBoundary saveMapSettingsInputBoundary;

    /**
     * Constructs a controller with the given input boundary.
     *
     * @param saveMapSettingsInputBoundary the interactor for saving map settings
     */
    public SaveMapSettingsController(SaveMapSettingsInputBoundary saveMapSettingsInputBoundary) {
        this.saveMapSettingsInputBoundary = saveMapSettingsInputBoundary;
    }

    /**
     * Triggers the "save map settings" use case with the given map state.
     *
     * @param centerLatitude  current map center latitude
     * @param centerLongitude current map center longitude
     * @param zoomLevel       current zoom level
     * @param weatherType     currently selected weather type (may be null)
     */
    public void saveMapSettings(double centerLatitude,
                                double centerLongitude,
                                int zoomLevel,
                                entity.WeatherType weatherType) {

        SaveMapSettingsInputData inputData =
                new SaveMapSettingsInputData(centerLatitude, centerLongitude, zoomLevel, weatherType);

        saveMapSettingsInputBoundary.saveMapSettings(inputData);
    }
}

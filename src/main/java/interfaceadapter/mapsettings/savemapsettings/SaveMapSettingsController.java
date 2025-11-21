package interfaceadapter.mapsettings.savemapsettings;

import usecase.mapsettings.savemapsettings.SaveMapSettingsInputBoundary;
import usecase.mapsettings.savemapsettings.SaveMapSettingsInputData;

/**
 * Controller for the "save map settings" use case.
 * <p>
 * The view calls this when the user wants to persist the current map view
 * (center latitude/longitude and zoom level).
 */
public class SaveMapSettingsController {

    private final SaveMapSettingsInputBoundary inputBoundary;

    /**
     * Constructs a controller that delegates to the given use case
     * input boundary.
     *
     * @param inputBoundary the "save map settings" use case entry point
     */
    public SaveMapSettingsController(SaveMapSettingsInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Saves the current map settings.
     * <p>
     * Typical usage from the UI layer:
     * <pre>
     * saveMapSettingsController.saveMapSettings(currentLat, currentLon, currentZoom);
     * </pre>
     *
     * @param centerLatitude  current map center latitude
     * @param centerLongitude current map center longitude
     * @param zoomLevel       current map zoom level
     */
    public void saveMapSettings(double centerLatitude,
                                double centerLongitude,
                                int zoomLevel) {
        SaveMapSettingsInputData inputData =
                new SaveMapSettingsInputData(centerLatitude, centerLongitude, zoomLevel);
        inputBoundary.saveMapSettings(inputData);
    }
}

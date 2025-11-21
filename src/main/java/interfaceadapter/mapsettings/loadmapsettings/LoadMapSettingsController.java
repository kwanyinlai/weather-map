package interfaceadapter.mapsettings.loadmapsettings;

import usecase.mapsettings.loadmapsettings.LoadMapSettingsInputBoundary;
import usecase.mapsettings.loadmapsettings.LoadMapSettingsInputData;

/**
 * Controller for the "load map settings" use case.
 *
 * <p>The view calls this when it wants to restore the last saved map state.</p>
 */
public class LoadMapSettingsController {

    private final LoadMapSettingsInputBoundary inputBoundary;

    /**
     * Constructs a controller that delegates to the given use case input boundary.
     *
     * @param inputBoundary the "load map settings" use case entry point
     */
    public LoadMapSettingsController(LoadMapSettingsInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Triggers a load of the last saved map settings.
     *
     * <p>Typical usage from the UI layer is to call this after the map view
     * is ready to be centered/zoomed.</p>
     */
    public void loadMapSettings() {
        LoadMapSettingsInputData inputData = new LoadMapSettingsInputData();
        inputBoundary.loadMapSettings(inputData);
    }
}

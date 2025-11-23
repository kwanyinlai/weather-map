package interfaceadapter.mapsettings.loadmapsettings;

import usecase.mapsettings.loadmapsettings.LoadMapSettingsInputBoundary;
import usecase.mapsettings.loadmapsettings.LoadMapSettingsInputData;

/**
 * Controller for the "load map settings" use case.
 *
 * <p>Called by the UI layer when the map view needs to restore the last
 * saved state. It translates the UI request into an input data object
 * and delegates to the interactor.</p>
 */
public final class LoadMapSettingsController {

    private final LoadMapSettingsInputBoundary loadMapSettingsInputBoundary;

    /**
     * Constructs a controller with the given input boundary.
     *
     * @param loadMapSettingsInputBoundary the interactor for loading map settings
     */
    public LoadMapSettingsController(LoadMapSettingsInputBoundary loadMapSettingsInputBoundary) {
        this.loadMapSettingsInputBoundary = loadMapSettingsInputBoundary;
    }

    /**
     * Triggers the "load map settings" use case.
     *
     * <p>Currently, no parameters are required, so an empty
     * {@link LoadMapSettingsInputData} is created and passed to the interactor.</p>
     */
    public void loadMapSettings() {
        LoadMapSettingsInputData inputData = new LoadMapSettingsInputData();
        loadMapSettingsInputBoundary.loadMapSettings(inputData);
    }
}

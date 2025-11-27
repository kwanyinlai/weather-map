package usecase.mapsettings.loadmapsettings;

import dataaccessinterface.SavedMapOverlaySettings;
import entity.Location;
import entity.WeatherType;

/**
 * Interactor for the "load map settings" use case.
 * Implements the application logic of restoring the last saved map state.
 */
public final class LoadMapSettingsUseCase implements LoadMapSettingsInputBoundary {

    private final SavedMapOverlaySettings savedMapOverlaySettings;
    private final LoadMapSettingsOutputBoundary outputBoundary;

    /**
     * Constructs an interactor with the required dependencies.
     *
     * @param savedMapOverlaySettings data access interface for saved map settings
     * @param outputBoundary          presenter to receive the result
     */
    public LoadMapSettingsUseCase(SavedMapOverlaySettings savedMapOverlaySettings,
                                     LoadMapSettingsOutputBoundary outputBoundary) {
        this.savedMapOverlaySettings = savedMapOverlaySettings;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void loadMapSettings(LoadMapSettingsInputData inputData) {
        try {
            // Check if there are any saved settings at all.
            if (!savedMapOverlaySettings.hasSavedSettings()) {
                // No saved state
                outputBoundary.presentNoSavedSettings();
                return;
            }

            LoadMapSettingsOutputData outputData = buildOutputData();

            // Hand the data to the presenter.
            outputBoundary.presentLoadedSettings(outputData);

        } catch (RuntimeException e) {
            // Any unexpected persistence error
            outputBoundary.presentLoadSettingsFailure("Failed to load saved map settings.");
        }
    }
    private LoadMapSettingsOutputData buildOutputData() {
        Location center = savedMapOverlaySettings.getSavedCenterLocation();
        int zoomLevel = savedMapOverlaySettings.getSavedZoomLevel();
        WeatherType weatherType = savedMapOverlaySettings.getSavedWeatherType();
        double latitude = center.getLatitude();
        double longitude = center.getLongitude();
        return new LoadMapSettingsOutputData(latitude, longitude, zoomLevel, weatherType);
    }
}

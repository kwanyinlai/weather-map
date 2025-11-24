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

            // Retrieve the saved center location, zoom level, and weather type.
            Location center = savedMapOverlaySettings.getSavedCenterLocation();
            int zoomLevel = savedMapOverlaySettings.getSavedZoomLevel();
            WeatherType weatherType = savedMapOverlaySettings.getSavedWeatherType();

            // Convert the location for the output data.
            double latitude = center.getLatitude();
            double longitude = center.getLongitude();

            // Wrap in an output data object.
            LoadMapSettingsOutputData outputData =
                    new LoadMapSettingsOutputData(latitude, longitude, zoomLevel, weatherType);

            // Hand the data to the presenter.
            outputBoundary.presentLoadedSettings(outputData);

        } catch (RuntimeException e) {
            // Any unexpected persistence error
            outputBoundary.presentLoadSettingsFailure("Failed to load saved map settings.");
        }
    }
}

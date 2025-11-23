package usecase.mapsettings.savemapsettings;

import dataaccessinterface.SavedMapOverlaySettings;
import entity.Location;

/**
 * Interactor for the "save map settings" use case.
 * Implements the application logic of persisting the current map state.
 */
public final class SaveMapSettingsUseCase implements SaveMapSettingsInputBoundary {

    private final SavedMapOverlaySettings savedMapOverlaySettings;
    private final SaveMapSettingsOutputBoundary outputBoundary;

    /**
     * Constructs an interactor with the required dependencies.
     *
     * @param savedMapOverlaySettings data access interface for map settings
     * @param outputBoundary          presenter to receive the result
     */
    public SaveMapSettingsUseCase(SavedMapOverlaySettings savedMapOverlaySettings,
                                     SaveMapSettingsOutputBoundary outputBoundary) {
        this.savedMapOverlaySettings = savedMapOverlaySettings;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void saveMapSettings(SaveMapSettingsInputData inputData) {
        // No input data at all
        if (inputData == null) {
            outputBoundary.presentSaveSettingsFailure("No map settings were provided.");
            return;
        }

        double latitude = inputData.getCenterLatitude();
        double longitude = inputData.getCenterLongitude();
        int zoomLevel = inputData.getZoomLevel();

        // Validation for coordinates
        if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
            outputBoundary.presentSaveSettingsFailure("Map center coordinates are invalid.");
            return;
        }

        try {
            // Create a Location entity from the input data
            Location centerLocation = new Location(latitude, longitude);

            savedMapOverlaySettings.save(centerLocation, zoomLevel);

            SaveMapSettingsOutputData outputData =
                    new SaveMapSettingsOutputData(true);

            outputBoundary.presentSavedSettings(outputData);

        } catch (RuntimeException e) {
            outputBoundary.presentSaveSettingsFailure("Failed to save map settings.");
        }
    }
}

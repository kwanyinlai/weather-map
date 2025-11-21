package interfaceadapter.mapsettings.savemapsettings;

import interfaceadapter.mapsettings.MapSettingsViewModel;
import interfaceadapter.mapsettings.MapSettingsViewModel.MapSettingsState;
import usecase.mapsettings.savemapsettings.SaveMapSettingsOutputBoundary;
import usecase.mapsettings.savemapsettings.SaveMapSettingsOutputData;

/**
 * Presenter for the "save map settings" use case.
 *
 * <p>Updates the {@link MapSettingsViewModel} based on whether saving
 * succeeded or failed.</p>
 */
public final class SaveMapSettingsPresenter implements SaveMapSettingsOutputBoundary {

    private final MapSettingsViewModel viewModel;

    /**
     * Constructs a presenter with the given view model.
     *
     * @param viewModel the view model representing the map settings in the UI
     */
    public SaveMapSettingsPresenter(MapSettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSavedSettings(SaveMapSettingsOutputData outputData) {
        if (outputData.isSuccess()) {
            // Mark saved settings, keeping the current coordinates/zoom.
            MapSettingsState current = viewModel.getState();

            double latitude = current != null ? current.getCenterLatitude() : 0.0;
            double longitude = current != null ? current.getCenterLongitude() : 0.0;
            int zoom = current != null ? current.getZoomLevel() : 1;

            viewModel.setMapSettings(
                    latitude,
                    longitude,
                    zoom,
                    true // we now have saved settings
            );

            // Clear any previous error.
            viewModel.setErrorMessage(null);
        } else {
            // This path won't usually be hit since failures go to presentSaveSettingsFailure.
            viewModel.setErrorMessage("Saving map settings did not complete successfully.");
        }
    }

    @Override
    public void presentSaveSettingsFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}

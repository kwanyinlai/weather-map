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
     * Creates a presenter that updates the given view model.
     *
     * @param viewModel the map settings view model
     */
    public SaveMapSettingsPresenter(MapSettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSavedSettings(SaveMapSettingsOutputData outputData) {
        if (outputData.isSuccess()) {
            // We assume the current state already reflects the coordinates/zoom
            // that were just saved (e.g. set by the load presenter or the view).
            MapSettingsState current = viewModel.getState();

            double latitude  = current != null ? current.getCenterLatitude()  : 0.0;
            double longitude = current != null ? current.getCenterLongitude() : 0.0;
            int zoom         = current != null ? current.getZoomLevel()       : 1;

            // Mark that we now definitely have saved settings; error cleared.
            viewModel.setSettings(latitude, longitude, zoom);
        } else {
            // This path might not usually be hit (failures often go to presentSaveSettingsFailure),
            // but we still handle it defensively.
            viewModel.setError("Saving map settings did not complete successfully.");
        }
    }

    @Override
    public void presentSaveSettingsFailure(String errorMessage) {
        viewModel.setError(errorMessage);
    }
}

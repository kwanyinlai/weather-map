package interfaceadapter.mapsettings.loadmapsettings;

import interfaceadapter.mapsettings.MapSettingsViewModel;
import interfaceadapter.mapsettings.MapSettingsViewModel.MapSettingsState;
import usecase.mapsettings.loadmapsettings.LoadMapSettingsOutputBoundary;
import usecase.mapsettings.loadmapsettings.LoadMapSettingsOutputData;

/**
 * Presenter for the "load map settings" use case.
 *
 * <p>Transforms the output data from the interactor into updates on the
 * {@link MapSettingsViewModel}, which the UI layer listens to.</p>
 */
public final class LoadMapSettingsPresenter implements LoadMapSettingsOutputBoundary {

    private final MapSettingsViewModel viewModel;

    /**
     * Creates a presenter that updates the given view model.
     *
     * @param viewModel the map settings view model
     */
    public LoadMapSettingsPresenter(MapSettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentLoadedSettings(LoadMapSettingsOutputData outputData) {
        // We have valid saved settings: update coordinates and zoom.
        viewModel.setSettings(
                outputData.getCenterLatitude(),
                outputData.getCenterLongitude(),
                outputData.getZoomLevel()
        );
        // setSettings already sets errorMessage to null in the state.
    }

    @Override
    public void presentNoSavedSettings() {
        // Represent the "no saved settings" situation explicitly:
        // hasSavedSettings = false, neutral coordinates/zoom, no error.
        MapSettingsState state = new MapSettingsState(
                false,   // hasSavedSettings
                0.0,     // centerLatitude
                0.0,     // centerLongitude
                1,       // zoomLevel
                null     // errorMessage
        );
        viewModel.setState(state);
    }

    @Override
    public void presentLoadSettingsFailure(String errorMessage) {
        // Keep existing settings (if any), surface the error.
        viewModel.setError(errorMessage);
    }
}

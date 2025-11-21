package interfaceadapter.mapsettings.loadmapsettings;

import interfaceadapter.mapsettings.MapSettingsViewModel;
import usecase.mapsettings.loadmapsettings.LoadMapSettingsOutputBoundary;
import usecase.mapsettings.loadmapsettings.LoadMapSettingsOutputData;

/**
 * Presenter for the "load map settings" use case.
 *
 * <p>It converts the raw output data from the interactor into updates on the
 * {@link MapSettingsViewModel}, which the UI listens to.</p>
 */
public class LoadMapSettingsPresenter implements LoadMapSettingsOutputBoundary {

    private final MapSettingsViewModel viewModel;

    /**
     * Creates a presenter that will write load results into the given view model.
     *
     * @param viewModel shared view model used by map-related views
     */
    public LoadMapSettingsPresenter(MapSettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Called when saved map settings are successfully loaded.
     *
     * @param outputData data describing the saved map state
     */
    @Override
    public void presentLoadedSettings(LoadMapSettingsOutputData outputData) {
        viewModel.setMapSettings(
                outputData.getCenterLatitude(),
                outputData.getCenterLongitude(),
                outputData.getZoomLevel()
        );
        // No error: loading worked.
        viewModel.setErrorMessage(null);
    }

    /**
     * Called when there are no saved settings available.
     * <p>This is not treated as an error: it is the "first run" / default case.</p>
     */
    @Override
    public void presentNoSavedSettings() {
        viewModel.clearSettings();
        viewModel.setErrorMessage(null);
    }

    /**
     * Called when loading map settings fails due to an error.
     *
     * @param errorMessage a user-friendly error message
     */
    @Override
    public void presentLoadSettingsFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}

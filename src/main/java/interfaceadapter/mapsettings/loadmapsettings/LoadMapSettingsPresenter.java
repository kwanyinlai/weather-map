package interfaceadapter.mapsettings.loadmapsettings;

import interfaceadapter.mapsettings.MapSettingsViewModel;
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
     * Constructs a presenter with the given view model.
     *
     * @param viewModel the view model that represents map settings in the UI
     */
    public LoadMapSettingsPresenter(MapSettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentLoadedSettings(LoadMapSettingsOutputData outputData) {
        // Populate the view model with the loaded settings.
        viewModel.setMapSettings(
                outputData.getCenterLatitude(),
                outputData.getCenterLongitude(),
                outputData.getZoomLevel(),
                true   // we have saved settings
        );

        // Clear any previous error, since loading was successful.
        viewModel.setErrorMessage(null);
    }

    @Override
    public void presentNoSavedSettings() {
        // No saved settings exist: keep default coordinates but mark it.
        // Defaults mirror MapSettingsViewModel's constructor.
        viewModel.setMapSettings(
                0.0,  // default latitude
                0.0,  // default longitude
                1,    // default zoom
                false // no saved settings
        );

        // Also clear any previous error message.
        viewModel.setErrorMessage(null);
    }

    @Override
    public void presentLoadSettingsFailure(String errorMessage) {
        // Do not change coordinates/zoom; just surface the error.
        viewModel.setErrorMessage(errorMessage);
    }
}

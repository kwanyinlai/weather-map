package interfaceadapter.mapsettings.savemapsettings;

import interfaceadapter.mapsettings.MapSettingsViewModel;
import usecase.mapsettings.savemapsettings.SaveMapSettingsOutputBoundary;
import usecase.mapsettings.savemapsettings.SaveMapSettingsOutputData;

/**
 * Presenter for the "save map settings" use case.
 * <p>
 * This presenter receives the result of the save operation from the
 * interactor and updates the {@link MapSettingsViewModel} accordingly.
 * <ul>
 *     <li>On success: clears any error message.</li>
 *     <li>On failure: sets a user-friendly error message.</li>
 * </ul>
 */
public class SaveMapSettingsPresenter implements SaveMapSettingsOutputBoundary {

    private final MapSettingsViewModel viewModel;

    /**
     * Creates a presenter that writes save results into the given view model.
     *
     * @param viewModel shared view model used by map-related views
     */
    public SaveMapSettingsPresenter(MapSettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Called by the use case when the save operation completes without
     * throwing an exception.
     *
     * @param outputData data describing the outcome of the save operation
     */
    @Override
    public void presentSavedSettings(SaveMapSettingsOutputData outputData) {
        if (outputData.isSuccess()) {
            // Save succeeded: clear any previous error message.
            viewModel.setErrorMessage(null);
        } else {
            // Defensive case: the interactor reports "not successful"
            // even though no exception was thrown.
            viewModel.setErrorMessage("Failed to save map settings.");
        }
    }

    /**
     * Called by the use case when saving fails due to an error
     * (e.g., persistence issues).
     *
     * @param errorMessage a user-friendly error message
     */
    @Override
    public void presentSaveSettingsFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}

package usecase.mapsettings.loadmapsettings;

/**
 * Output boundary for the "load map settings" use case.
 * Implemented by the presenter.
 */
public interface LoadMapSettingsOutputBoundary {

    /**
     * Called when saved map settings are successfully loaded.
     *
     * @param outputData data describing the saved map state
     */
    void presentLoadedSettings(LoadMapSettingsOutputData outputData);

    /**
     * Called when there are no saved map settings available.
     * For example, when the app is opened for the first time.
     */
    void presentNoSavedSettings();

    /**
     * Called when loading map settings fails due to an error
     * (e.g., unexpected persistence error).
     *
     * @param errorMessage a user-friendly error message
     */
    void presentLoadSettingsFailure(String errorMessage);
}

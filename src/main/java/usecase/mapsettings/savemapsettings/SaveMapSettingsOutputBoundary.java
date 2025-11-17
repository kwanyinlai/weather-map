package usecase.mapsettings.savemapsettings;

/**
 * Output boundary for the "save map settings" use case.
 * Implemented by the presenter.
 */
public interface SaveMapSettingsOutputBoundary {

    /**
     * Called when map settings have been successfully saved.
     *
     * @param outputData data describing the outcome of the save operation
     */
    void presentSavedSettings(SaveMapSettingsOutputData outputData);

    /**
     * Called when saving map settings fails
     * (e.g., invalid input or persistence error).
     *
     * @param errorMessage a user-friendly error message
     */
    void presentSaveSettingsFailure(String errorMessage);
}

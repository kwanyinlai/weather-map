package usecase.mapsettings.savemapsettings;

/**
 * Data returned by the "save map settings" use case to the presenter.
 */
public final class SaveMapSettingsOutputData {

    private final boolean success;

    /**
     * Constructs output data representing the result of saving map settings.
     *
     * @param success whether the settings were saved successfully
     */
    public SaveMapSettingsOutputData(boolean success) {
        this.success = success;
    }

    /**
     * Returns whether the save operation succeeded.
     *
     * @return true if settings were saved successfully; false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
}

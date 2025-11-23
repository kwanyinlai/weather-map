package usecase.mapsettings.savemapsettings;

/**
 * Input boundary for the "save map settings" use case.
 * Called by the controller when the user changes the map view
 * to persist that state.
 */
public interface SaveMapSettingsInputBoundary {

    /**
     * Saves the current map settings.
     *
     * @param inputData data describing the current map state
     */
    void saveMapSettings(SaveMapSettingsInputData inputData);
}

package usecase.mapsettings.loadmapsettings;

/**
 * Input boundary for the "load map settings" use case.
 * Called by the controller when the map screen needs the last saved state.
 */
public interface LoadMapSettingsInputBoundary {

    /**
     * Loads the last saved map settings (if any).
     *
     * @param inputData input data for this use case (currently unused but
     *                  kept for future flexibility).
     */
    void loadMapSettings(LoadMapSettingsInputData inputData);
}

package usecase.mapsettings.loadmapsettings;

/**
 * Input data for the "load map settings" use case.
 * Currently, this use case does not require any parameters, but this class
 * is kept to preserve the Clean Architecture pattern and to allow for
 * future extensions (for example, loading settings for a specific map).
 */
public final class LoadMapSettingsInputData {

    /**
     * Constructs input data for loading map settings.
     * No parameters are required at the moment.
     */
    public LoadMapSettingsInputData() {
        // Intentionally empty.
    }
}

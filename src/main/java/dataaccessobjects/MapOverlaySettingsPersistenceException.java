package dataaccessobjects;

/**
 * Indicates a failure to persist or load map overlay settings from disk.
 */
public class MapOverlaySettingsPersistenceException extends RuntimeException {

    public MapOverlaySettingsPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

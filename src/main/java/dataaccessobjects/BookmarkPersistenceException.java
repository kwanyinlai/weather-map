package dataaccessobjects;

/**
 * Indicates a failure to persist bookmark data to disk.
 */
public class BookmarkPersistenceException extends RuntimeException {

    public BookmarkPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
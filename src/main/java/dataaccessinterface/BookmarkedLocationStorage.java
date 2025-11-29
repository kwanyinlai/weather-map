package dataaccessinterface;

import entity.BookmarkedLocation;
import java.util.List;

public interface BookmarkedLocationStorage {
    /**
     * Returns all bookmarked locations currently persisted.
     *
     * @return An immutable list of persisted bookmarks (possibly empty).
     */
    List<BookmarkedLocation> getBookmarkedLocations();

    /**
     * Persists the provided bookmarked location.
     *
     * @param bookmarkedLocation The location to add.
     */
    void addBookmarkedLocation(BookmarkedLocation bookmarkedLocation);

    /**
     * Removes the first persisted bookmark matching the given entity
     * (by name and coordinates).
     *
     * @param bookmarkedLocation The bookmark to remove; matching is performed
     *                           on name and latitude/longitude.
     * @return {@code true} if a persisted bookmark was found and removed;
     * {@code false} otherwise.
     */
    boolean removeBookmarkedLocation(BookmarkedLocation bookmarkedLocation);
}

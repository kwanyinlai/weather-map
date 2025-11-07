package dataaccessinterface;

import entity.BookmarkedLocation;
import java.util.List;

public interface BookmarkedLocationStorage {
    // Get the list of all bookmarked locations.
    List<BookmarkedLocation> getBookmarkedLocation();

    // Add the bookmarked location.
    void bookmarkLocation(BookmarkedLocation bookmarkedLocation);

}

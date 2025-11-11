package dataaccessinterface;

import entity.BookmarkedLocation;
import java.util.List;

public interface BookmarkedLocationStorage {
    // Get the list of all bookmarked locations.
    List<BookmarkedLocation> getBookmarkedLocations();

    // Add the bookmarked location.
    void addBookmarkedLocation(BookmarkedLocation bookmarkedLocation);

}

package interfaceadapter.bookmark.addbookmark;

import usecase.bookmark.addbookmark.AddBookmarkInputBoundary;
import usecase.bookmark.addbookmark.AddBookmarkInputData;

/**
 * Controller for the "add bookmark" use case.
 *
 * <p>Called by the UI layer when the user requests to add a new bookmark.
 * It translates primitive input values into an {@link AddBookmarkInputData}
 * object and delegates the work to the use case interactor via the
 * {@link AddBookmarkInputBoundary}.</p>
 */
public final class AddBookmarkController {

    private final AddBookmarkInputBoundary addBookmarkInputBoundary;

    /**
     * Constructs a controller with the given input boundary.
     *
     * @param addBookmarkInputBoundary the interactor for adding bookmarks
     */
    public AddBookmarkController(AddBookmarkInputBoundary addBookmarkInputBoundary) {
        this.addBookmarkInputBoundary = addBookmarkInputBoundary;
    }

    /**
     * Triggers the "add bookmark" use case.
     *
     * @param name      the user-visible name of the bookmark
     * @param latitude  the latitude of the bookmarked location
     * @param longitude the longitude of the bookmarked location
     */
    public void addBookmark(String name, double latitude, double longitude) {
        AddBookmarkInputData inputData =
                new AddBookmarkInputData(name, latitude, longitude);
        addBookmarkInputBoundary.addBookmark(inputData);
    }
}

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
     * Creates a controller that delegates to the given input boundary.
     *
     * @param addBookmarkInputBoundary the add–bookmark use case interactor
     */
    public AddBookmarkController(AddBookmarkInputBoundary addBookmarkInputBoundary) {
        this.addBookmarkInputBoundary = addBookmarkInputBoundary;
    }

    /**
     * Requests that a bookmark be added with the given attributes.
     *
     * @param name      name of the bookmark
     * @param latitude  latitude of the bookmarked location
     * @param longitude longitude of the bookmarked location
     */
    public void addBookmark(String name, double latitude, double longitude) {
        AddBookmarkInputData inputData =
                new AddBookmarkInputData(name, latitude, longitude);
        addBookmarkInputBoundary.addBookmark(inputData);
    }
}

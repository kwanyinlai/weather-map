package interfaceadapter.bookmark.removebookmark;

import usecase.bookmark.removebookmark.RemoveBookmarkInputBoundary;
import usecase.bookmark.removebookmark.RemoveBookmarkInputData;

/**
 * Controller for the "remove bookmark" use case.
 *
 * <p>Called by the UI layer when the user requests to delete a bookmark.
 * It translates primitive input values into a
 * {@link RemoveBookmarkInputData} object and delegates to the use case
 * interactor.</p>
 */
public final class RemoveBookmarkController {

    private final RemoveBookmarkInputBoundary removeBookmarkInputBoundary;

    /**
     * Creates a controller that delegates to the given input boundary.
     *
     * @param removeBookmarkInputBoundary the remove–bookmark use case interactor
     */
    public RemoveBookmarkController(RemoveBookmarkInputBoundary removeBookmarkInputBoundary) {
        this.removeBookmarkInputBoundary = removeBookmarkInputBoundary;
    }

    /**
     * Requests that a bookmark be removed.
     *
     * @param name      name of the bookmark
     * @param latitude  latitude of the bookmarked location
     * @param longitude longitude of the bookmarked location
     */
    public void removeBookmark(String name, double latitude, double longitude) {
        RemoveBookmarkInputData inputData =
                new RemoveBookmarkInputData(name, latitude, longitude);
        removeBookmarkInputBoundary.removeBookmark(inputData);
    }
}

package interfaceadapter.bookmark.removebookmark;

import usecase.bookmark.removebookmark.RemoveBookmarkInputBoundary;
import usecase.bookmark.removebookmark.RemoveBookmarkInputData;

/**
 * Controller for the "remove bookmark" use case.
 *
 * <p>Called by the UI layer when the user requests that a bookmark be
 * deleted. It translates primitive values into an input data object and
 * delegates to the interactor.</p>
 */
public final class RemoveBookmarkController {

    private final RemoveBookmarkInputBoundary removeBookmarkInputBoundary;

    /**
     * Constructs a controller with the given input boundary.
     *
     * @param removeBookmarkInputBoundary the interactor for removing bookmarks
     */
    public RemoveBookmarkController(RemoveBookmarkInputBoundary removeBookmarkInputBoundary) {
        this.removeBookmarkInputBoundary = removeBookmarkInputBoundary;
    }

    /**
     * Triggers the "remove bookmark" use case.
     *
     * @param name      name of the bookmark to remove
     * @param latitude  latitude of the bookmarked location
     * @param longitude longitude of the bookmarked location
     */
    public void removeBookmark(String name, double latitude, double longitude) {
        RemoveBookmarkInputData inputData =
                new RemoveBookmarkInputData(name, latitude, longitude);
        removeBookmarkInputBoundary.removeBookmark(inputData);
    }
}

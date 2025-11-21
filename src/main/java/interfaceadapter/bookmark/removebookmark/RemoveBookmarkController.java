package interfaceadapter.bookmark.removebookmark;

import usecase.bookmark.removebookmark.RemoveBookmarkInputBoundary;
import usecase.bookmark.removebookmark.RemoveBookmarkInputData;

/**
 * Controller for the "remove bookmark" flow.
 * <p>
 * This class accepts UI requests to remove a bookmark and forwards them
 * to the corresponding use case.
 */
public class RemoveBookmarkController {

    private final RemoveBookmarkInputBoundary inputBoundary;

    /**
     * Constructs a controller that delegates to the "remove bookmark" use case.
     *
     * @param inputBoundary the use case entry point
     */
    public RemoveBookmarkController(RemoveBookmarkInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Requests removal of the specified bookmark.
     * <p>
     * The bookmark is identified by its name and coordinates; this mirrors
     * the semantics used by the data access layer.
     *
     * @param name      name of the bookmark
     * @param latitude  latitude of the location
     * @param longitude longitude of the location
     */
    public void removeBookmark(String name, double latitude, double longitude) {
        RemoveBookmarkInputData inputData =
                new RemoveBookmarkInputData(name, latitude, longitude);
        inputBoundary.removeBookmark(inputData);
    }
}

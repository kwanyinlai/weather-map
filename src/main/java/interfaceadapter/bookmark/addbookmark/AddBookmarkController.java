package interfaceadapter.bookmark.addbookmark;

import usecase.bookmark.addbookmark.AddBookmarkInputBoundary;
import usecase.bookmark.addbookmark.AddBookmarkInputData;

/**
 * Controller for the "add bookmark" flow.
 * <p>
 * This class lives in the interface-adapter layer and translates
 * UI-level operations (e.g., a user clicking an "Add bookmark" button)
 * into calls to the {@link AddBookmarkInputBoundary} use case.
 */
public class AddBookmarkController {

    private final AddBookmarkInputBoundary inputBoundary;

    /**
     * Constructs a controller that delegates to the given use case input boundary.
     *
     * @param inputBoundary the "add bookmark" use case entry point
     */
    public AddBookmarkController(AddBookmarkInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Requests that a new bookmark be added for the specified location.
     * <p>
     * Typical usage from the UI layer:
     * <pre>
     * addBookmarkController.addBookmark(name, latitude, longitude);
     * </pre>
     *
     * @param name      human-readable name for the bookmark
     * @param latitude  latitude of the location
     * @param longitude longitude of the location
     */
    public void addBookmark(String name, double latitude, double longitude) {
        AddBookmarkInputData inputData =
                new AddBookmarkInputData(name, latitude, longitude);
        inputBoundary.addBookmark(inputData);
    }
}

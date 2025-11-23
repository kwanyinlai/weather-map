package interfaceadapter.bookmark.listbookmark;

import usecase.bookmark.listbookmark.ListBookmarksInputBoundary;
import usecase.bookmark.listbookmark.ListBookmarksInputData;

/**
 * Controller for the "list bookmarks" use case.
 *
 * <p>Called by the UI layer when the user wants to view all bookmarks.
 * It creates a {@link ListBookmarksInputData} object and delegates to
 * the use case interactor.</p>
 */
public final class ListBookmarksController {

    private final ListBookmarksInputBoundary listBookmarksInputBoundary;

    /**
     * Creates a controller that delegates to the given input boundary.
     *
     * @param listBookmarksInputBoundary the list–bookmarks use case interactor
     */
    public ListBookmarksController(ListBookmarksInputBoundary listBookmarksInputBoundary) {
        this.listBookmarksInputBoundary = listBookmarksInputBoundary;
    }

    /**
     * Requests that all bookmarks be listed.
     */
    public void listBookmarks() {
        ListBookmarksInputData inputData = new ListBookmarksInputData();
        listBookmarksInputBoundary.listBookmarks(inputData);
    }
}

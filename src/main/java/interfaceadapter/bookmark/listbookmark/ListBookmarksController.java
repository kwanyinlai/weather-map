package interfaceadapter.bookmark.listbookmark;

import usecase.bookmark.listbookmark.ListBookmarksInputBoundary;
import usecase.bookmark.listbookmark.ListBookmarksInputData;

/**
 * Controller for the "list bookmarks" use case.
 *
 * <p>Called by the UI when the user navigates to the bookmarks view or
 * refreshes it. This controller constructs an (empty) input data object
 * and delegates to the interactor.</p>
 */
public final class ListBookmarksController {

    private final ListBookmarksInputBoundary listBookmarksInputBoundary;

    /**
     * Constructs a controller with the given input boundary.
     *
     * @param listBookmarksInputBoundary the interactor for listing bookmarks
     */
    public ListBookmarksController(ListBookmarksInputBoundary listBookmarksInputBoundary) {
        this.listBookmarksInputBoundary = listBookmarksInputBoundary;
    }

    /**
     * Triggers the "list bookmarks" use case.
     */
    public void listBookmarks() {
        ListBookmarksInputData inputData = new ListBookmarksInputData();
        listBookmarksInputBoundary.listBookmarks(inputData);
    }
}

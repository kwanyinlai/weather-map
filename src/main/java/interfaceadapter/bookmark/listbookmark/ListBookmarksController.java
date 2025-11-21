package interfaceadapter.bookmark.listbookmark;

import usecase.bookmark.listbookmark.ListBookmarksInputBoundary;
import usecase.bookmark.listbookmark.ListBookmarksInputData;

/**
 * Controller for the "list bookmarks" flow.
 * <p>
 * This controller triggers the use case to retrieve all persisted bookmarks.
 * It does not know how the result will be displayed; that is the job of
 * the presenter and view model.
 */
public class ListBookmarksController {

    private final ListBookmarksInputBoundary inputBoundary;

    /**
     * Constructs a controller that delegates to the "list bookmarks" use case.
     *
     * @param inputBoundary the use case entry point
     */
    public ListBookmarksController(ListBookmarksInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Requests that all current bookmarks be listed.
     * <p>
     * Typical usage from the UI layer is to call this on startup or when
     * the bookmarks view needs to be refreshed.
     */
    public void listBookmarks() {
        ListBookmarksInputData inputData = new ListBookmarksInputData();
        inputBoundary.listBookmarks(inputData);
    }
}

package usecase.bookmark.removebookmark;

/**
 * Output boundary for the "remove bookmark" use case.
 * Implemented by the presenter.
 */
public interface RemoveBookmarkOutputBoundary {

    /**
     * Called when a bookmark has been successfully removed.
     *
     * @param outputData data describing the removed bookmark
     */
    void presentRemovedBookmark(RemoveBookmarkOutputData outputData);

    /**
     * Called when removing a bookmark fails (e.g., bookmark not found,
     * persistence error, invalid input, etc.).
     *
     * @param errorMessage a user-friendly error message
     */
    void presentRemoveBookmarkFailure(String errorMessage);
}

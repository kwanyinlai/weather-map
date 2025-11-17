package usecase.bookmark.addbookmark;

/**
 * Output boundary for the "add bookmark" use case.
 * Implemented by the presenter.
 */
public interface AddBookmarkOutputBoundary {

    /**
     * Called when a bookmark has been successfully added.
     *
     * @param outputData data describing the newly added bookmark
     */
    void presentAddedBookmark(AddBookmarkOutputData outputData);

    /**
     * Called when adding a bookmark fails (e.g., invalid input).
     *
     * @param errorMessage a user-friendly error message
     */
    void presentAddBookmarkFailure(String errorMessage);
}

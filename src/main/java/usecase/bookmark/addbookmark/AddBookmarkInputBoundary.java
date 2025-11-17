package usecase.bookmark.addbookmark;

/**
 * Input boundary for the "add bookmark" use case.
 * Called by the controller when the user requests to add a bookmark.
 */
public interface AddBookmarkInputBoundary {

    /**
     * Adds a new bookmarked location based on the provided input data.
     *
     * @param inputData data describing the bookmark to be added
     */
    void addBookmark(AddBookmarkInputData inputData);
}

package usecase.bookmark.removebookmark;

/**
 * Input boundary for the "remove bookmark" use case.
 * Called by the controller when the user requests to remove a bookmark.
 */
public interface RemoveBookmarkInputBoundary {

    /**
     * Removes a bookmarked location based on the provided input data.
     *
     * @param inputData data that identifies the bookmark to remove
     */
    void removeBookmark(RemoveBookmarkInputData inputData);
}

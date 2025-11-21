package interfaceadapter.bookmark.listbookmark;

import interfaceadapter.bookmark.BookmarksViewModel;
import usecase.bookmark.listbookmark.ListBookmarksOutputBoundary;
import usecase.bookmark.listbookmark.ListBookmarksOutputData;

/**
 * Presenter for the "list bookmarks" use case.
 * <p>
 * This presenter receives the complete set of bookmarks from the use case
 * and pushes them into the {@link BookmarksViewModel}.
 */
public class ListBookmarksPresenter implements ListBookmarksOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Creates a presenter that will write list results into the given view model.
     *
     * @param viewModel shared view model used by bookmark-related views
     */
    public ListBookmarksPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Called by the use case when bookmarks have been successfully loaded.
     *
     * @param outputData data describing the list of bookmarks to present
     */
    @Override
    public void presentBookmarks(ListBookmarksOutputData outputData) {
        // The output data already contains a List<BookmarkedLocation>.
        viewModel.setBookmarks(outputData.getBookmarks());
        viewModel.setErrorMessage(null);
    }

    /**
     * Called when listing bookmarks fails (for example if there is a
     * persistence error).
     *
     * @param errorMessage user-friendly error message
     */
    @Override
    public void presentListBookmarksFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}

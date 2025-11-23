package interfaceadapter.bookmark.listbookmark;

import interfaceadapter.bookmark.BookmarksViewModel;
import usecase.bookmark.listbookmark.ListBookmarksOutputBoundary;
import usecase.bookmark.listbookmark.ListBookmarksOutputData;

/**
 * Presenter for the "list bookmarks" use case.
 *
 * <p>Updates the {@link BookmarksViewModel} with the full list of
 * bookmarks or an error message.</p>
 */
public final class ListBookmarksPresenter implements ListBookmarksOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Creates a presenter that updates the given view model.
     *
     * @param viewModel the bookmarks view model
     */
    public ListBookmarksPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentBookmarks(ListBookmarksOutputData outputData) {
        viewModel.setBookmarks(outputData.getBookmarks());
        viewModel.setErrorMessage(null);
    }

    @Override
    public void presentListBookmarksFailure(String errorMessage) {
        // Keep any existing bookmarks, display the error.
        viewModel.setErrorMessage(errorMessage);
    }
}

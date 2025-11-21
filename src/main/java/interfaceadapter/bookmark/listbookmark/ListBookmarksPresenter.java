package interfaceadapter.bookmark.listbookmark;

import interfaceadapter.bookmark.BookmarksViewModel;
import usecase.bookmark.listbookmark.ListBookmarksOutputBoundary;
import usecase.bookmark.listbookmark.ListBookmarksOutputData;

/**
 * Presenter for the "list bookmarks" use case.
 *
 * <p>Transforms the output of the list–bookmarks interactor into updates
 * on the {@link BookmarksViewModel}.</p>
 */
public final class ListBookmarksPresenter implements ListBookmarksOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Constructs a presenter with the given bookmarks view model.
     *
     * @param viewModel the view model representing bookmarks in the UI
     */
    public ListBookmarksPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentBookmarks(ListBookmarksOutputData outputData) {
        // Simply push the list of bookmarks into the view model.
        viewModel.setBookmarks(outputData.getBookmarks());
        // Clear any error if listing succeeded.
        viewModel.setErrorMessage(null);
    }

    @Override
    public void presentListBookmarksFailure(String errorMessage) {
        // Preserve the existing bookmarks (if desired) and only set the error.
        viewModel.setErrorMessage(errorMessage);
    }
}

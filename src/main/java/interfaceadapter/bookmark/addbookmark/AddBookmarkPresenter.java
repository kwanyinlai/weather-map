package interfaceadapter.bookmark.addbookmark;

import entity.BookmarkedLocation;
import interfaceadapter.bookmark.BookmarksViewModel;
import interfaceadapter.bookmark.BookmarksViewModel.BookmarksState;
import usecase.bookmark.addbookmark.AddBookmarkOutputBoundary;
import usecase.bookmark.addbookmark.AddBookmarkOutputData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Presenter for the "add bookmark" use case.
 *
 * <p>Transforms use-case output data into updates on the
 * {@link BookmarksViewModel}, which the view observes.</p>
 */
public final class AddBookmarkPresenter implements AddBookmarkOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Creates a presenter that updates the given view model.
     *
     * @param viewModel the bookmarks view model
     */
    public AddBookmarkPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentAddedBookmark(AddBookmarkOutputData outputData) {
        BookmarkedLocation added = new BookmarkedLocation(
                outputData.getName(),
                outputData.getLatitude(),
                outputData.getLongitude()
        );

        BookmarksState currentState = viewModel.getState();
        List<BookmarkedLocation> current =
                currentState == null
                        ? Collections.emptyList()
                        : currentState.getBookmarks();

        List<BookmarkedLocation> updated = new ArrayList<>(current);
        updated.add(added);

        viewModel.setBookmarks(updated);
        // Clear any previous error.
        viewModel.setErrorMessage(null);
    }

    @Override
    public void presentAddBookmarkFailure(String errorMessage) {
        // Not changing the existing list, surface the error.
        viewModel.setErrorMessage(errorMessage);
    }
}

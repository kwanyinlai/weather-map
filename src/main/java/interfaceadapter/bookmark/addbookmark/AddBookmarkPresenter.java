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
 * <p>Transforms the output of the add–bookmark interactor into updates
 * on the {@link BookmarksViewModel}, which the UI listens to.</p>
 */
public final class AddBookmarkPresenter implements AddBookmarkOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Constructs a presenter with the given bookmarks view model.
     *
     * @param viewModel the view model representing the bookmarks in the UI
     */
    public AddBookmarkPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentAddedBookmark(AddBookmarkOutputData outputData) {
        // Convert the output data into a BookmarkedLocation entity.
        BookmarkedLocation added = new BookmarkedLocation(
                outputData.getName(),
                outputData.getLatitude(),
                outputData.getLongitude()
        );

        // Start from the current list of bookmarks (if any) and append the new one.
        BookmarksState currentState = viewModel.getState();
        List<BookmarkedLocation> current =
                (currentState == null || currentState.getBookmarks() == null)
                        ? Collections.emptyList()
                        : currentState.getBookmarks();

        List<BookmarkedLocation> updated = new ArrayList<>(current);
        updated.add(added);

        viewModel.setBookmarks(updated);
        // Clear any previous error message now that the operation succeeded.
        viewModel.setErrorMessage(null);
    }

    @Override
    public void presentAddBookmarkFailure(String errorMessage) {
        // Keep the existing list of bookmarks but expose the error.
        viewModel.setErrorMessage(errorMessage);
    }
}

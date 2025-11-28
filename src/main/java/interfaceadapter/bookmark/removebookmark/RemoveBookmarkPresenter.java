package interfaceadapter.bookmark.removebookmark;

import entity.BookmarkedLocation;
import interfaceadapter.bookmark.BookmarksViewModel;
import interfaceadapter.bookmark.BookmarksViewModel.BookmarksState;
import usecase.bookmark.removebookmark.RemoveBookmarkOutputBoundary;
import usecase.bookmark.removebookmark.RemoveBookmarkOutputData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Presenter for the "remove bookmark" use case.
 *
 * <p>Updates the {@link BookmarksViewModel} when a bookmark is successfully
 * removed or when removal fails.</p>
 */
public final class RemoveBookmarkPresenter implements RemoveBookmarkOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Creates a presenter that updates the given view model.
     *
     * @param viewModel the bookmarks view model
     */
    public RemoveBookmarkPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentRemovedBookmark(RemoveBookmarkOutputData outputData) {
        if (!outputData.isRemoved()) {
            // Interactor says nothing was removed; treat as a failure.
            viewModel.setErrorMessage("The bookmark could not be removed.");
            return;
        }

        BookmarksState currentState = viewModel.getState();
        List<BookmarkedLocation> current =
                currentState == null
                        ? Collections.emptyList()
                        : currentState.getBookmarks();

        List<BookmarkedLocation> updated = new ArrayList<>();
        for (BookmarkedLocation bookmark : current) {
            boolean sameName = bookmark.getName().equals(outputData.getName());
            boolean sameLat =
                    Double.compare(bookmark.getLatitude(), outputData.getLatitude()) == 0;
            boolean sameLon =
                    Double.compare(bookmark.getLongitude(), outputData.getLongitude()) == 0;

            // Skip the one that was removed.
            if (sameName && sameLat && sameLon) {
                continue;
            }
            updated.add(bookmark);
        }

        viewModel.setBookmarks(updated);
        // Clear any previous error.
        viewModel.setErrorMessage(null);
    }

    @Override
    public void presentRemoveBookmarkFailure(String errorMessage) {
        // Show the error.
        viewModel.setErrorMessage(errorMessage);
    }
}

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
 * <p>Applies the result of the remove–bookmark interactor to the
 * {@link BookmarksViewModel}.</p>
 */
public final class RemoveBookmarkPresenter implements RemoveBookmarkOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Constructs a presenter with the given bookmarks view model.
     *
     * @param viewModel the view model representing bookmarks in the UI
     */
    public RemoveBookmarkPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentRemovedBookmark(RemoveBookmarkOutputData outputData) {
        if (!outputData.isRemoved()) {
            // Defensive: if the use case reports no removal, expose a generic error.
            viewModel.setErrorMessage("The bookmark could not be removed.");
            return;
        }

        BookmarksState currentState = viewModel.getState();
        List<BookmarkedLocation> current =
                (currentState == null || currentState.getBookmarks() == null)
                        ? Collections.emptyList()
                        : currentState.getBookmarks();

        List<BookmarkedLocation> updated = new ArrayList<>();

        for (BookmarkedLocation bookmark : current) {
            boolean sameName = bookmark.getName().equals(outputData.getName());
            boolean sameLat = Double.compare(bookmark.getLatitude(), outputData.getLatitude()) == 0;
            boolean sameLon = Double.compare(bookmark.getLongitude(), outputData.getLongitude()) == 0;

            // Skip the one that matches the removed bookmark.
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
        // Keep the existing bookmarks but show the error.
        viewModel.setErrorMessage(errorMessage);
    }
}

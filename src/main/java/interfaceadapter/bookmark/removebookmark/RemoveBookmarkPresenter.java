package interfaceadapter.bookmark.removebookmark;

import entity.BookmarkedLocation;
import interfaceadapter.bookmark.BookmarksViewModel;
import usecase.bookmark.removebookmark.RemoveBookmarkOutputBoundary;
import usecase.bookmark.removebookmark.RemoveBookmarkOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the "remove bookmark" use case.
 * <p>
 * This presenter updates the {@link BookmarksViewModel} to reflect removal
 * of a bookmark, or displays an error message if the removal failed.
 */
public class RemoveBookmarkPresenter implements RemoveBookmarkOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Creates a presenter that will update the given view model on
     * successful or failed bookmark removals.
     *
     * @param viewModel shared view model used by bookmark-related views
     */
    public RemoveBookmarkPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Called by the use case when a bookmark has been successfully removed.
     * <p>
     * The presenter removes the matching bookmark from the list currently
     * stored in the view model. Matching is done on name and coordinates,
     * mirroring the semantics of the data access layer.
     *
     * @param outputData data describing the bookmark that was removed
     */
    @Override
    public void presentRemovedBookmark(RemoveBookmarkOutputData outputData) {
        if (!outputData.isRemoved()) {
            // Defensive: in case the output indicates that nothing was removed.
            viewModel.setErrorMessage("Failed to remove bookmark.");
            return;
        }

        List<BookmarkedLocation> current = new ArrayList<>(viewModel.getBookmarks());
        current.removeIf(b ->
                b.getName().equals(outputData.getName())
                        && Double.compare(b.getLatitude(), outputData.getLatitude()) == 0
                        && Double.compare(b.getLongitude(), outputData.getLongitude()) == 0
        );

        viewModel.setBookmarks(current);
        viewModel.setErrorMessage(null);
    }

    /**
     * Called when removing a bookmark fails.
     *
     * @param errorMessage user-friendly error message
     */
    @Override
    public void presentRemoveBookmarkFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}

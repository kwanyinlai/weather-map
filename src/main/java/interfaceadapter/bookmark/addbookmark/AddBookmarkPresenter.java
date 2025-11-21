package interfaceadapter.bookmark.addbookmark;

import entity.BookmarkedLocation;
import interfaceadapter.bookmark.BookmarksViewModel;
import usecase.bookmark.addbookmark.AddBookmarkOutputBoundary;
import usecase.bookmark.addbookmark.AddBookmarkOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the "add bookmark" use case.
 * <p>
 * This class converts the raw output data from the use case into
 * updates on the {@link BookmarksViewModel}, which the UI listens to.
 * It does not know anything about Swing or specific UI widgets.
 */
public class AddBookmarkPresenter implements AddBookmarkOutputBoundary {

    private final BookmarksViewModel viewModel;

    /**
     * Creates a presenter that will update the given view model.
     *
     * @param viewModel shared view model used by bookmark-related views
     */
    public AddBookmarkPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Called by the use case when a bookmark has been successfully added.
     * <p>
     * The presenter converts the {@link AddBookmarkOutputData} into a
     * {@link BookmarkedLocation} and appends it to the list exposed by
     * the view model.
     *
     * @param outputData data describing the newly added bookmark
     */
    @Override
    public void presentAddedBookmark(AddBookmarkOutputData outputData) {
        BookmarkedLocation newBookmark =
                new BookmarkedLocation(
                        outputData.getName(),
                        outputData.getLatitude(),
                        outputData.getLongitude()
                );

        List<BookmarkedLocation> updated = new ArrayList<>(viewModel.getBookmarks());
        updated.add(newBookmark);

        viewModel.setBookmarks(updated);
        viewModel.setErrorMessage(null); // clear any previous error
    }

    /**
     * Called by the use case when adding a bookmark fails.
     * <p>
     * The presenter simply forwards the error message to the view model,
     * which allows the UI to display it to the user.
     *
     * @param errorMessage user-friendly error message
     */
    @Override
    public void presentAddBookmarkFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}

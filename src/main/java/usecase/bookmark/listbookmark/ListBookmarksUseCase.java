package usecase.bookmark.listbookmark;

import dataaccessinterface.BookmarkedLocationStorage;
import entity.BookmarkedLocation;
import java.util.List;

/**
 * Interactor for the "list bookmarks" use case.
 * Implements the application logic of retrieving all bookmarks.
 */
public final class ListBookmarksInteractor implements ListBookmarksInputBoundary {

    private final BookmarkedLocationStorage bookmarkedLocationStorage;
    private final ListBookmarksOutputBoundary outputBoundary;

    /**
     * Constructs an interactor with the required dependencies.
     *
     * @param bookmarkedLocationStorage data access interface for bookmarks
     * @param outputBoundary            presenter to receive the result
     */
    public ListBookmarksInteractor(BookmarkedLocationStorage bookmarkedLocationStorage,
                                   ListBookmarksOutputBoundary outputBoundary) {
        this.bookmarkedLocationStorage = bookmarkedLocationStorage;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void listBookmarks(ListBookmarksInputData inputData) {

        try {
            List <BookmarkedLocation> bookmarks = bookmarkedLocationStorage.getBookmarkedLocations();

            ListBookmarksOutputData outputData = new ListBookmarksOutputData(bookmarks);

            outputBoundary.presentBookmarks(outputData);

        } catch (RuntimeException e) {
            // If the DAO fails (e.g. disk read error),
            outputBoundary.presentListBookmarksFailure("Failed to load bookmarks.");
        }
    }
}
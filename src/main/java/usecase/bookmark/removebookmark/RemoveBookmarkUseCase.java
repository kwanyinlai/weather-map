package usecase.bookmark.removebookmark;

import dataaccessinterface.BookmarkedLocationStorage;
import dataaccessobjects.BookmarkPersistenceException;
import entity.BookmarkedLocation;

/**
 * Interactor for the "remove bookmark" use case.
 * Implements the application logic of removing an existing bookmark.
 */
public final class RemoveBookmarkUseCase implements RemoveBookmarkInputBoundary {

    private final BookmarkedLocationStorage bookmarkedLocationStorage;
    private final RemoveBookmarkOutputBoundary outputBoundary;

/**
 * Constructs an interactor with the required dependencies.
 *
 * @param bookmarkedLocationStorage data access interface for bookmarks
 * @param outputBoundary            presenter to receive the result
 */
    public RemoveBookmarkUseCase(BookmarkedLocationStorage bookmarkedLocationStorage,
                                 RemoveBookmarkOutputBoundary outputBoundary) {

    this.bookmarkedLocationStorage = bookmarkedLocationStorage;
    this.outputBoundary = outputBoundary;
    }

    @Override
    public void removeBookmark(RemoveBookmarkInputData inputData) {

        if (inputData == null) {
            outputBoundary.presentRemoveBookmarkFailure("No bookmark data was provided.");
            return;
        }

        String name = inputData.getName();
        double latitude = inputData.getLatitude();
        double longitude = inputData.getLongitude();

        if (name == null || name.isBlank()) {
            outputBoundary.presentRemoveBookmarkFailure("Bookmark name must not be empty.");
            return;
        }

        if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
            outputBoundary.presentRemoveBookmarkFailure("Bookmark coordinates are invalid.");
            return;
        }

        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            outputBoundary.presentRemoveBookmarkFailure("Bookmark coordinates are out of range.");
            return;
        }

        BookmarkedLocation toRemove = new  BookmarkedLocation(name, latitude, longitude);

        final boolean removed;
        try {
            removed = bookmarkedLocationStorage.removeBookmarkedLocation(toRemove);
        } catch (BookmarkPersistenceException e) {
            outputBoundary.presentRemoveBookmarkFailure("Failed to update bookmarks.");
            return;
        }

        if(!removed) {
            outputBoundary.presentRemoveBookmarkFailure("Bookmarks not found.");
            return;
        }

        RemoveBookmarkOutputData outputData =
                new RemoveBookmarkOutputData(name, latitude, longitude, removed);

        outputBoundary.presentRemovedBookmark(outputData);

    }

}
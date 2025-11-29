package usecase.bookmark.addbookmark;

import dataaccessinterface.BookmarkedLocationStorage;
import entity.BookmarkedLocation;

/**
 * Interactor for the "add bookmark" use case.
 * Implements the application logic of adding a new bookmark.
 */
public final class AddBookmarkUseCase implements AddBookmarkInputBoundary {

    private final BookmarkedLocationStorage bookmarkedLocationStorage;
    private final AddBookmarkOutputBoundary outputBoundary;

    /**
     * Constructs an interactor with the required dependencies.
     *
     * @param bookmarkedLocationStorage data access interface for bookmarks
     * @param outputBoundary            presenter to receive the result
     */
    public AddBookmarkUseCase(BookmarkedLocationStorage bookmarkedLocationStorage,
                                 AddBookmarkOutputBoundary outputBoundary) {
        this.bookmarkedLocationStorage = bookmarkedLocationStorage;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void addBookmark(AddBookmarkInputData inputData) {

        String name = inputData.getName();
        double latitude = inputData.getLatitude();
        double longitude = inputData.getLongitude();

        if (name == null || name.isBlank()) {
            outputBoundary.presentAddBookmarkFailure("Bookmark name must not be empty.");
            return;
        }

        if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
            outputBoundary.presentAddBookmarkFailure("Bookmark coordinates are invalid.");
            return;
        }

        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            outputBoundary.presentAddBookmarkFailure("Bookmark coordinates are out of range.");
            return;
        }

        // Prevent saving duplicate bookmarks (same name and coordinates).
        try {
            java.util.List<BookmarkedLocation> existing =
                    bookmarkedLocationStorage.getBookmarkedLocations();

            for (BookmarkedLocation existingBookmark : existing) {
                if (existingBookmark.getName().equals(name)
                        && Double.compare(existingBookmark.getLatitude(), latitude) == 0
                        && Double.compare(existingBookmark.getLongitude(), longitude) == 0) {
                    // Duplicate occurence.
                    outputBoundary.presentAddBookmarkFailure(
                            "A bookmark with the same name and location already exists.");
                    return;
                }
            }
        } catch (RuntimeException e) {
            // If we cannot read existing bookmarks fail
            outputBoundary.presentAddBookmarkFailure("Failed to read existing bookmarks.");
            return;
        }

        BookmarkedLocation bookmarkedLocation =
                new BookmarkedLocation(name, latitude, longitude);

        try {
            bookmarkedLocationStorage.addBookmarkedLocation(bookmarkedLocation);
        } catch (RuntimeException e) {
            // Failure to write the bookmark to the disk.
            outputBoundary.presentAddBookmarkFailure("Failed to save bookmark.");
            return;
        }

        AddBookmarkOutputData outputData =
                new AddBookmarkOutputData(name, latitude, longitude);
        outputBoundary.presentAddedBookmark(outputData);
    }
}
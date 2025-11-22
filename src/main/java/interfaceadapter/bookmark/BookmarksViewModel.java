package interfaceadapter.bookmark;

import interfaceadapter.ViewModel;
import entity.BookmarkedLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ViewModel for the bookmarks screen.
 */
public final class BookmarksViewModel
        extends ViewModel<BookmarksViewModel.BookmarksState> {

    public static final String VIEW_NAME = "bookmarks";

    /**
     * The property name used when firing state change events.
     */
    public static final String STATE_PROPERTY = "state";

    /**
     * Constructs a new BookmarksViewModel with an initial empty state.
     */
    public BookmarksViewModel() {
        super(VIEW_NAME);
        // Empty list of bookmarks and no error.
        this.setState(new BookmarksState(Collections.emptyList(), null));
    }

    /**
     * Returns the current bookmarks state (may be null before initialization).
     */
    @Override
    public BookmarksState getState() {
        return super.getState();
    }

    /**
     * Sets the entire bookmarks state and notifies listeners.
     */
    @Override
    public void setState(BookmarksState state) {
        super.setState(state);
        firePropertyChange(STATE_PROPERTY);
    }

    /**
     * Convenience method for presenters: replace the bookmarks list while
     * preserving the current error message (if any).
     */
    public void setBookmarks(List<BookmarkedLocation> bookmarks) {
        BookmarksState current = getState();
        List<BookmarkedLocation> safeList =
                (bookmarks == null) ? Collections.emptyList() : new ArrayList<>(bookmarks);

        BookmarksState newState = new BookmarksState(
                safeList,
                current == null ? null : current.getErrorMessage()
        );

        setState(newState);
    }

    /**
     * Convenience method for presenters: update the error message while
     * preserving the current bookmarks list.
     */
    public void setErrorMessage(String errorMessage) {
        BookmarksState current = getState();
        List<BookmarkedLocation> bookmarks =
                (current == null || current.getBookmarks() == null)
                        ? Collections.emptyList()
                        : current.getBookmarks();

        BookmarksState newState = new BookmarksState(bookmarks, errorMessage);
        setState(newState);
    }

    /**
     * Immutable state object representing everything the bookmarks view needs
     * to render itself.
     */
    public static final class BookmarksState {

        private final List<BookmarkedLocation> bookmarks;
        private final String errorMessage;

        /**
         * Creates a new bookmarks state.
         *
         * @param bookmarks    list of bookmarked locations to display
         * @param errorMessage optional error message, or null if none
         */
        public BookmarksState(List<BookmarkedLocation> bookmarks,
                              String errorMessage) {
            // Defensive copy, then wrap as unmodifiable to keep the state immutable.
            this.bookmarks = (bookmarks == null)
                    ? Collections.emptyList()
                    : Collections.unmodifiableList(new ArrayList<>(bookmarks));
            this.errorMessage = errorMessage;
        }

        /**
         * Returns an unmodifiable list of bookmarked locations.
         */
        public List<BookmarkedLocation> getBookmarks() {
            return bookmarks;
        }

        /**
         * Returns the current error message, or null if none.
         */
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}

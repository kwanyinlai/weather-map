package interfaceadapter.bookmark;

import interfaceadapter.ViewModel;
import entity.BookmarkedLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ViewModel for the bookmarks screen.
 *
 * <p>This class extends the generic {@link ViewModel} used in the project and
 * specializes it for bookmark-related state. It exposes convenience methods
 * for presenters to update the list of bookmarks and any associated error
 * messages, and notifies any listeners via property change events.</p>
 */
public final class BookmarksViewModel extends ViewModel<BookmarksViewModel.BookmarksState> {

    /**
     * A logical name for this view. Useful if your application switches
     * between multiple views.
     */
    public static final String VIEW_NAME = "bookmarks";

    /**
     * The property name used when firing state change events.
     * Listeners can check this name to know that the bookmark state has changed.
     */
    public static final String STATE_PROPERTY = "state";

    /**
     * Constructs a new BookmarksViewModel with an initial empty state.
     */
    public BookmarksViewModel() {
        super(VIEW_NAME);
        // Start with an empty list of bookmarks and no error.
        this.setState(new BookmarksState(Collections.emptyList(), null));
    }

    /**
     * Updates the list of bookmarks in the state and notifies listeners.
     *
     * @param bookmarks the new list of bookmarks to display (may be null,
     *                  in which case it is treated as an empty list)
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
        firePropertyChange(STATE_PROPERTY);
    }

    /**
     * Updates the error message in the state (for example, when a persistence
     * error occurs) and notifies listeners.
     *
     * @param errorMessage the new error message to display, or null to clear it
     */
    public void setErrorMessage(String errorMessage) {
        BookmarksState current = getState();
        List<BookmarkedLocation> currentBookmarks =
                (current == null || current.getBookmarks() == null)
                        ? Collections.emptyList()
                        : current.getBookmarks();

        BookmarksState newState = new BookmarksState(currentBookmarks, errorMessage);
        setState(newState);
        firePropertyChange(STATE_PROPERTY);
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
         * @param bookmarks    the list of bookmarked locations to display
         * @param errorMessage an optional error message for the UI to show,
         *                     or null if there is no error
         */
        public BookmarksState(List<BookmarkedLocation> bookmarks, String errorMessage) {
            // Internally keep a defensive copy to preserve immutability.
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

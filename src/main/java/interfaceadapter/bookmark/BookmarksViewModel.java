package interfaceadapter.bookmark;

import entity.BookmarkedLocation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * View model for all bookmark-related UI state.
 * <p>
 * This class sits in the interface-adapter layer and exposes a UI-friendly
 * representation of the application's bookmark state:
 * <ul>
 *     <li>The current list of {@link BookmarkedLocation} objects to display.</li>
 *     <li>An optional error message to show to the user.</li>
 * </ul>
 * <p>
 * Swing views can register {@link PropertyChangeListener}s to be notified when
 * either the bookmarks list or the error message changes, and then redraw
 * themselves accordingly.
 */
public class BookmarksViewModel {

    /**
     * Name of the property used when firing change events for the bookmarks list.
     * <p>
     * Listeners can use this constant to check which property changed.
     */
    public static final String BOOKMARKS_PROPERTY = "bookmarks";

    /**
     * Name of the property used when firing change events for the error message.
     * <p>
     * Listeners can use this constant to check which property changed.
     */
    public static final String ERROR_PROPERTY = "errorMessage";

    /**
     * Helper object that manages registration of listeners and firing
     * {@link java.beans.PropertyChangeEvent}s when the view model changes.
     */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Current bookmarks to be displayed by the UI.
     */
    private List<BookmarkedLocation> bookmarks = new ArrayList<>();

    /**
     * Current error message to show to the user, or {@code null}
     * if there is no error to display.
     */
    private String errorMessage = null;

    /**
     * Returns an immutable snapshot of the current bookmark list.
     *
     * @return unmodifiable list of {@link BookmarkedLocation} objects
     */
    public List<BookmarkedLocation> getBookmarks() {
        return Collections.unmodifiableList(bookmarks);
    }

    /**
     * Returns the current error message.
     *
     * @return error message, or {@code null} if there is no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Replaces the current bookmarks list and notifies any registered listeners.
     *
     * @param newBookmarks new list of bookmarks to expose to the UI
     */
    public void setBookmarks(List<BookmarkedLocation> newBookmarks) {
        List<BookmarkedLocation> old = this.bookmarks;
        this.bookmarks = new ArrayList<>(newBookmarks);
        support.firePropertyChange(BOOKMARKS_PROPERTY, old, this.bookmarks);
    }

    /**
     * Updates the error message and notifies any registered listeners.
     *
     * @param newErrorMessage new error message to display; may be {@code null}
     *                        to indicate that there is currently no error
     */
    public void setErrorMessage(String newErrorMessage) {
        String old = this.errorMessage;
        this.errorMessage = newErrorMessage;
        support.firePropertyChange(ERROR_PROPERTY, old, newErrorMessage);
    }

    /**
     * Registers a new listener that will be notified whenever one of the
     * view model's properties changes.
     *
     * @param listener listener to register
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Unregisters a previously registered property change listener.
     *
     * @param listener listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}

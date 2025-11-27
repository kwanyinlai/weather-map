package view;

import entity.BookmarkedLocation;
import interfaceadapter.bookmark.BookmarksViewModel;
import interfaceadapter.bookmark.BookmarksViewModel.BookmarksState;
import interfaceadapter.bookmark.addbookmark.AddBookmarkController;
import interfaceadapter.bookmark.removebookmark.RemoveBookmarkController;
import interfaceadapter.bookmark.listbookmark.ListBookmarksController;
import interfaceadapter.bookmark.visitbookmark.VisitBookmarkController;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.List;

/**
 * Swing-based view for displaying and managing bookmarked locations.
 *
 * <p>This view listens to a {@link BookmarksViewModel} for state changes and
 * delegates user actions to the appropriate controllers for the add, remove,
 * and list–bookmarks use cases.</p>
 */
public final class BookmarksView extends JPanel implements PropertyChangeListener {

    private final transient BookmarksViewModel viewModel;
    private final transient AddBookmarkController addBookmarkController;
    private final transient RemoveBookmarkController removeBookmarkController;
    private final transient VisitBookmarkController visitBookmarkController;

    // UI components
    private final JTextField nameField = new JTextField(15);
    private final JFormattedTextField latitudeField;
    private final JFormattedTextField longitudeField;

    private final DefaultListModel<String> bookmarksListModel = new DefaultListModel<>();
    private final JList<String> bookmarksList = new JList<>(bookmarksListModel);

    private final JButton addButton = new JButton("Add");
    private final JButton removeButton = new JButton("Remove");
    private final JButton visitButton = new JButton("Visit");

    private final JLabel errorLabel = new JLabel();

    /**
     * Creates a new bookmarks view.
     *
     * @param viewModel                the bookmarks ViewModel to observe
     * @param addBookmarkController    controller for the add–bookmark use case
     * @param removeBookmarkController controller for the remove–bookmark use case
     * @param listBookmarksController  controller for the list–bookmarks use case
     */
    public BookmarksView(BookmarksViewModel viewModel,
                         AddBookmarkController addBookmarkController,
                         RemoveBookmarkController removeBookmarkController,
                         ListBookmarksController listBookmarksController,
                         VisitBookmarkController visitBookmarkController) {

        this.viewModel = viewModel;
        this.addBookmarkController = addBookmarkController;
        this.removeBookmarkController = removeBookmarkController;
        this.visitBookmarkController = visitBookmarkController;

        // Create formatted text fields for latitude and longitude that accept doubles
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        NumberFormatter latFormatter = new NumberFormatter(numberFormat);
        latFormatter.setValueClass(Double.class);
        latFormatter.setAllowsInvalid(true);
        latFormatter.setMinimum(-90.0);
        latFormatter.setMaximum(90.0);
        latitudeField = new JFormattedTextField(latFormatter);
        latitudeField.setColumns(8);
        latitudeField.setFocusLostBehavior(JFormattedTextField.PERSIST);

        NumberFormatter lonFormatter = new NumberFormatter(numberFormat);
        lonFormatter.setValueClass(Double.class);
        lonFormatter.setAllowsInvalid(true);
        lonFormatter.setMinimum(-180.0);
        lonFormatter.setMaximum(180.0);
        longitudeField = new JFormattedTextField(lonFormatter);
        longitudeField.setColumns(8);
        longitudeField.setFocusLostBehavior(JFormattedTextField.PERSIST);

        setLayout(new BorderLayout());
        buildUi();

        // Listen for state changes.
        this.viewModel.addPropertyChangeListener(this);

        // Initialise from current state, if any.
        updateFromState(this.viewModel.getState());

        // Load bookmarks from storage on startup.
        listBookmarksController.listBookmarks();
    }

    /**
     * Builds the Swing UI components and layout.
     */
    private void buildUi() {
        // Top panel: inputs for a new / existing bookmark, stacked vertically.
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // Name row
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);

        // Latitude row
        JPanel latPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        latPanel.add(new JLabel("Lat:"));
        latPanel.add(latitudeField);

        // Longitude row
        JPanel lonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lonPanel.add(new JLabel("Lon:"));
        lonPanel.add(longitudeField);

        // Vertically display the rows
        inputPanel.add(namePanel);
        inputPanel.add(latPanel);
        inputPanel.add(lonPanel);

        add(inputPanel, BorderLayout.NORTH);


        // Center: list of bookmarks.
        bookmarksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookmarksList);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: buttons + error label.
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(visitButton);

        bottomPanel.add(buttonsPanel, BorderLayout.NORTH);

        errorLabel.setForeground(Color.RED);
        bottomPanel.add(errorLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
        // Wire button actions.
        hookUpActions();
    }

    /**
     * Wires the button actions to the controllers.
     */
    private void hookUpActions() {
        addButton.addActionListener(e -> handleAddBookmark());
        removeButton.addActionListener(e -> handleRemoveBookmark());
        visitButton.addActionListener(e -> handleVisitBookmark());
    }

    /**
     * Handles the add bookmark button action.
     */
    private void handleAddBookmark() {
        String name = nameField.getText().trim();
        String latText = latitudeField.getText().trim();
        String lonText = longitudeField.getText().trim();

        try {
            double latitude = parseCoordinate(latitudeField.getValue(), latText);
            double longitude = parseCoordinate(longitudeField.getValue(), lonText);
            addBookmarkController.addBookmark(name, latitude, longitude);
        } catch (NumberFormatException | NullPointerException ex) {
            errorLabel.setText("Latitude and longitude must be valid numbers.");
        }
    }

    /**
     * Handles the remove bookmark button action.
     */
    private void handleRemoveBookmark() {
        int selectedIndex = bookmarksList.getSelectedIndex();
        if (selectedIndex >= 0 && tryRemoveSelectedBookmark(selectedIndex)) {
            return;
        }
        handleRemoveUsingFields();
    }

    /**
     * Attempts to remove a bookmark from the selected list item.
     *
     * @param selectedIndex the index of the selected bookmark
     * @return true if the bookmark was removed, false otherwise
     */
    private boolean tryRemoveSelectedBookmark(int selectedIndex) {
        BookmarksState state = viewModel.getState();
        if (state == null || state.getBookmarks() == null
                || selectedIndex >= state.getBookmarks().size()) {
            return false;
        }

        BookmarkedLocation selected = state.getBookmarks().get(selectedIndex);
        removeBookmarkController.removeBookmark(
                selected.getName(),
                selected.getLatitude(),
                selected.getLongitude()
        );
        return true;
    }

    /**
     * Handles the visit bookmark button action.
     */
    private void handleVisitBookmark() {
        int selectedIndex = bookmarksList.getSelectedIndex();
        if (selectedIndex < 0) {
            errorLabel.setText("Select a bookmark to visit.");
            return;
        }

        BookmarkedLocation selected = getSelectedBookmark(selectedIndex);
        if (selected == null) {
            errorLabel.setText("Unable to determine selected bookmark.");
            return;
        }

        visitBookmarkController.visitBookmark(
                selected.getLatitude(),
                selected.getLongitude()
        );
    }

    /**
     * Gets the selected bookmark from the state.
     *
     * @param selectedIndex the index of the selected bookmark
     * @return the selected bookmark, or null if not available
     */
    private BookmarkedLocation getSelectedBookmark(int selectedIndex) {
        BookmarksState state = viewModel.getState();
        if (state == null || state.getBookmarks() == null
                || selectedIndex >= state.getBookmarks().size()) {
            return null;
        }
        return state.getBookmarks().get(selectedIndex);
    }

    /**
     * Parses a coordinate value from either a formatted field value or text.
     *
     * @param value the formatted field value
     * @param text the text fallback
     * @return the parsed double value
     * @throws NumberFormatException if parsing fails
     */
    private double parseCoordinate(Object value, String text) throws NumberFormatException {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.parseDouble(text);
    }

    /**
     * Helper used by the remove button when it needs to rely on the text fields.
     */
    private void handleRemoveUsingFields() {
        String name = nameField.getText().trim();
        String latText = latitudeField.getText().trim();
        String lonText = longitudeField.getText().trim();
        try {
            double latitude = parseCoordinate(latitudeField.getValue(), latText);
            double longitude = parseCoordinate(longitudeField.getValue(), lonText);
            removeBookmarkController.removeBookmark(name, latitude, longitude);
        } catch (NumberFormatException | NullPointerException ex) {
            errorLabel.setText("Latitude and longitude must be valid numbers.");
        }
    }

    /**
     * Updates the latitude and longitude text fields to match the given coordinates.
     * Intended to be called when the map viewport changes.
     */
    public void setCoordinates(double latitude, double longitude) {
        // If you're worried about threads, you can wrap this in SwingUtilities.invokeLater.
        latitudeField.setValue(latitude);
        longitudeField.setValue(longitude);
    }

    /**
     * Reacts to changes in the {@link BookmarksViewModel} state.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (BookmarksViewModel.STATE_PROPERTY.equals(evt.getPropertyName())
                || "state".equals(evt.getPropertyName())) {
            Object newValue = evt.getNewValue();
            if (newValue instanceof BookmarksState) {
                updateFromState((BookmarksState) newValue);
            }
        }
    }

    /**
     * Updates the Swing components to reflect the given state.
     */
    private void updateFromState(BookmarksState state) {
        if (state == null) {
            bookmarksListModel.clear();
            errorLabel.setText("");
            return;
        }

        // Update list contents.
        bookmarksListModel.clear();
        List<BookmarkedLocation> bookmarks = state.getBookmarks();
        if (bookmarks != null) {
            for (BookmarkedLocation b : bookmarks) {
                String entry = String.format("%s (%.6f, %.6f)",
                        b.getName(), b.getLatitude(), b.getLongitude());
                bookmarksListModel.addElement(entry);
            }
        }

        // Update error label.
        String error = state.getErrorMessage();
        errorLabel.setText(error == null ? "" : error);
    }
}

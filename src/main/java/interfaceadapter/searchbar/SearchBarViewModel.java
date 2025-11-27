package interfaceadapter.searchbar;
import entity.LocationWithName;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class SearchBarViewModel {
    private final List<LocationWithName> searchResults = new ArrayList<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public static final String SEARCH_RESULTS_PROPERTY = "searchResults";

    public void updateResults(List<LocationWithName> newResults) {
        List<LocationWithName> oldResults = new ArrayList<>(this.searchResults);
        this.searchResults.clear();
        this.searchResults.addAll(newResults);
        support.firePropertyChange(SEARCH_RESULTS_PROPERTY, oldResults, this.searchResults);
    }

    public List<LocationWithName> getSearchResults() {
        return new ArrayList<>(searchResults);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}

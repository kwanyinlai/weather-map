package usecase.searchbar;

import entity.LocationWithName;
import java.util.List;

public class SearchBarOutputData {
    private final List<LocationWithName> results;

    public SearchBarOutputData(List<LocationWithName> results) {
        this.results = results;
    }

    public List<LocationWithName> getResults() {
        return results;
    }

}

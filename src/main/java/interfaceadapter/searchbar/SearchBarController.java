package interfaceadapter.searchbar;
import usecase.searchbar.SearchBarInputBoundary;
import usecase.searchbar.SearchBarInputData;
public class SearchBarController {
    private final SearchBarInputBoundary useCase;

    public SearchBarController( SearchBarInputBoundary useCase) {
        this.useCase = useCase;
    }

    public void handleSearch(String query) {
        SearchBarInputData inputData = new SearchBarInputData(query);
        useCase.execute(inputData);
    }
}

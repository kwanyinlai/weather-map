package interfaceadapter.searchbar;
import usecase.searchbar.SearchBarOutputBoundary;
import usecase.searchbar.SearchBarOutputData;

public class SearchBarPresenter implements SearchBarOutputBoundary {
    private final SearchBarViewModel viewModel;

    public SearchBarPresenter(SearchBarViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        public void present(SearchBarOutputData outputData) {
            viewModel.updateResults(outputData.getResults());
        }
}

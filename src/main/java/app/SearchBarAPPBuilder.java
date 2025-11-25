package app;
import dataaccessinterface.GeocodingAPI;
import dataaccessobjects.OpenWeatherGeocodingAPI;
import entity.LocationWithName;
import usecase.searchbar.SearchBarUsecase;
import usecase.searchbar.SearchBarOutputBoundary;
import usecase.searchbar.SearchBarOutputData;
import view.SearchBarView;
import interfaceadapter.searchbar.SearchBarController;
import interfaceadapter.searchbar.SearchBarPresenter;
import interfaceadapter.searchbar.SearchBarViewModel;
import javax.swing.*;
import java.util.List;
public class SearchBarAPPBuilder {
    public static void main(String[] args) {
            SearchBarViewModel viewModel = new SearchBarViewModel();

            SearchBarOutputBoundary presenter = new SearchBarPresenter(viewModel);

            GeocodingAPI geocodingAPI = new OpenWeatherGeocodingAPI();

            SearchBarUsecase useCase = new SearchBarUsecase(geocodingAPI, presenter);

            SearchBarController controller = new SearchBarController(useCase);

            SwingUtilities.invokeLater(() -> {
                SearchBarView searchView = new SearchBarView(viewModel,controller);
                searchView.setVisible(true);
            });
        }
}

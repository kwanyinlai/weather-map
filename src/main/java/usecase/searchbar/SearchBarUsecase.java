package usecase.searchbar;

import dataaccessinterface.GeocodingAPI;
import entity.LocationWithName;

import java.util.List;

public class SearchBarUsecase implements SearchBarInputBoundary{
    private final GeocodingAPI geocodingAPI;
    private final SearchBarOutputBoundary outputBoundary;


    public SearchBarUsecase(GeocodingAPI geocodingAPI, SearchBarOutputBoundary outputBoundary) {
        this.geocodingAPI = geocodingAPI;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(SearchBarInputData inputData) {
        List<LocationWithName> results = geocodingAPI.geocode(inputData.getQuery());
        outputBoundary.present(new SearchBarOutputData(results));
    }
}

import entity.LocationWithName;
import interfaceadapter.searchbar.SearchBarController;
import interfaceadapter.searchbar.SearchBarPresenter;
import interfaceadapter.searchbar.SearchBarViewModel;
import dataaccessinterface.GeocodingAPI;
import usecase.searchbar.SearchBarUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class MockGeocodingAPI implements GeocodingAPI {
    @Override
    public List<LocationWithName> geocode(String query) {
        List<LocationWithName> results = new ArrayList<>();
        String trimmedQuery = query.trim().toLowerCase();


        switch (trimmedQuery) {
            case "london":
                results.add(new LocationWithName("London, GB", 51.5074, -0.1278));
                break;
            case "paris":
                results.add(new LocationWithName("Paris, FR", 48.8566, 2.3522));
                break;
            case "new york":
                results.add(new LocationWithName("New York, US", 40.7128, -74.0060));
                break;
            case "tokyo":
                results.add(new LocationWithName("Tokyo, JP", 35.6762, 139.6503));
                break;
            default:
                break;
        }
        return results;
    }
}

class SearchBarTest {
    private SearchBarViewModel viewModel;
    private SearchBarController controller;

    @BeforeEach
    void setUp() {
        GeocodingAPI mockGeocodingAPI = new MockGeocodingAPI();
        viewModel = new SearchBarViewModel();
        SearchBarPresenter presenter = new SearchBarPresenter(viewModel);
        SearchBarUsecase useCase = new SearchBarUsecase(mockGeocodingAPI, presenter);
        controller = new SearchBarController(useCase);
    }

    @Test
    void testSearchValidLondon() {
        try {
            controller.handleSearch("London");
            boolean hasLondon = false;
            for (LocationWithName result : viewModel.getSearchResults()) {
                if (result.getName().contains("London") && result.getName().contains("GB")) {
                    hasLondon = true;
                    assertEquals(51.5074, result.getLatitude());
                    assertEquals(-0.1278, result.getLongitude());
                }
            }
            assertTrue(hasLondon);
            assertFalse(viewModel.getSearchResults().isEmpty());
        } catch (Exception e) {
            fail("Search London failed: " + e.getMessage());
        }
    }

    @Test
    void testSearchValidParisAndTokyo() {
        try {
            controller.handleSearch("Paris");
            boolean hasParis = viewModel.getSearchResults().get(0).getName().contains("Paris");
            controller.handleSearch("Tokyo");
            boolean hasTokyo = viewModel.getSearchResults().get(0).getName().contains("Tokyo");

            assertTrue(hasParis && hasTokyo);
        } catch (Exception e) {
            fail("Search Paris/Tokyo failed: " + e.getMessage());
        }
    }

    @Test
    void testSearchBlankQuery() {
        try {
            controller.handleSearch("   ");
            assertTrue(viewModel.getSearchResults().isEmpty());

            controller.handleSearch("");
            assertTrue(viewModel.getSearchResults().isEmpty());
        } catch (Exception e) {
            fail("Search blank query failed: " + e.getMessage());
        }
    }

    @Test
    void testSearchInvalidCity() {
        try {
            controller.handleSearch("InvalidCityXYZ123");
            assertTrue(viewModel.getSearchResults().isEmpty());

            controller.handleSearch("12345");
            assertTrue(viewModel.getSearchResults().isEmpty());
        } catch (Exception e) {
            fail("Search invalid city failed: " + e.getMessage());
        }
    }

    @Test
    void testMultipleSearches() {
        try {
            controller.handleSearch("New York");
            boolean firstValid = viewModel.getSearchResults().get(0).getName().contains("New York");

            controller.handleSearch("FakeCity");
            boolean secondEmpty = viewModel.getSearchResults().isEmpty();

            controller.handleSearch("London");
            boolean thirdValid = viewModel.getSearchResults().get(0).getName().contains("London");

            assertTrue(firstValid && secondEmpty && thirdValid);
        } catch (Exception e) {
            fail("Multiple searches failed: " + e.getMessage());
        }
    }

    @Test
    void testSearchResultStructure() {
        try {
            controller.handleSearch("Paris");
            LocationWithName result = viewModel.getSearchResults().get(0);

            assertNotNull(result.getName());
            assertNotNull(result.getCoordinate());
            assertEquals(48.8566, result.getLatitude());
            assertEquals(2.3522, result.getLongitude());
            assertEquals("Paris, FR", result.getName());
        } catch (Exception e) {
            fail("Result structure test failed: " + e.getMessage());
        }
    }
}
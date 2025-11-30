import dataaccessinterface.SavedMapOverlaySettings;
import dataaccessobjects.InDiskMapOverlaySettingsStorage;
import dataaccessobjects.MapOverlaySettingsPersistenceException;
import entity.Location;
import entity.WeatherType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import usecase.mapsettings.loadmapsettings.*;
import usecase.mapsettings.savemapsettings.*;
import interfaceadapter.mapsettings.loadmapsettings.LoadMapSettingsController;
import interfaceadapter.mapsettings.loadmapsettings.LoadMapSettingsPresenter;
import interfaceadapter.mapsettings.loadmapsettings.AutoLoadMapSettingsPresenter;
import interfaceadapter.mapsettings.savemapsettings.SaveMapSettingsController;
import interfaceadapter.mapsettings.savemapsettings.SaveMapSettingsPresenter;
import interfaceadapter.mapsettings.MapSettingsViewModel;
import entity.Viewport;
import usecase.weatherlayers.layers.ChangeLayerInputBoundary;
import usecase.weatherlayers.layers.ChangeLayerInputData;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for map settings-related use cases and storage.
 */
class MapSettingsUseCaseTest {

    // ========== Test Doubles ==========

    /**
     * In-memory implementation of SavedMapOverlaySettings for testing.
     */
    private static class InMemoryMapSettingsStorage implements SavedMapOverlaySettings {
        private Location savedCenter;
        private Integer savedZoom;
        private WeatherType savedWeatherType;
        private boolean hasSettings = false;

        @Override
        public boolean hasSavedSettings() {
            return hasSettings && savedCenter != null && savedZoom != null;
        }

        @Override
        public Location getSavedCenterLocation() {
            if (!hasSettings) {
                throw new MapOverlaySettingsPersistenceException("No settings", new RuntimeException());
            }
            return savedCenter;
        }

        @Override
        public int getSavedZoomLevel() {
            if (!hasSettings) {
                throw new MapOverlaySettingsPersistenceException("No settings", new RuntimeException());
            }
            return savedZoom;
        }

        @Override
        public WeatherType getSavedWeatherType() {
            if (!hasSettings) {
                return null;
            }
            return savedWeatherType;
        }

        @Override
        public void save(Location centerLocation, int zoomLevel, WeatherType weatherType) {
            this.savedCenter = centerLocation;
            this.savedZoom = zoomLevel;
            this.savedWeatherType = weatherType;
            this.hasSettings = true;
        }
    }

    /**
     * Failing storage implementation that throws exceptions.
     */
    private static class FailingMapSettingsStorage implements SavedMapOverlaySettings {
        @Override
        public boolean hasSavedSettings() {
            throw new RuntimeException("Storage read failure");
        }

        @Override
        public Location getSavedCenterLocation() {
            throw new MapOverlaySettingsPersistenceException("Read failure", new RuntimeException());
        }

        @Override
        public int getSavedZoomLevel() {
            throw new MapOverlaySettingsPersistenceException("Read failure", new RuntimeException());
        }

        @Override
        public WeatherType getSavedWeatherType() {
            throw new MapOverlaySettingsPersistenceException("Read failure", new RuntimeException());
        }

        @Override
        public void save(Location centerLocation, int zoomLevel, WeatherType weatherType) {
            throw new MapOverlaySettingsPersistenceException("Write failure", new RuntimeException());
        }
    }

    /**
     * Test double for SaveMapSettingsOutputBoundary that records calls.
     */
    private static class TestSaveMapSettingsPresenter implements SaveMapSettingsOutputBoundary {
        private SaveMapSettingsOutputData successData;
        private String failureMessage;

        @Override
        public void presentSavedSettings(SaveMapSettingsOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void presentSaveSettingsFailure(String errorMessage) {
            this.failureMessage = errorMessage;
        }

        public SaveMapSettingsOutputData getSuccessData() {
            return successData;
        }

        public String getFailureMessage() {
            return failureMessage;
        }

        public void reset() {
            successData = null;
            failureMessage = null;
        }
    }

    /**
     * Test double for LoadMapSettingsOutputBoundary that records calls.
     */
    private static class TestLoadMapSettingsPresenter implements LoadMapSettingsOutputBoundary {
        private LoadMapSettingsOutputData successData;
        private boolean noSettingsCalled;
        private String failureMessage;

        @Override
        public void presentLoadedSettings(LoadMapSettingsOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void presentNoSavedSettings() {
            this.noSettingsCalled = true;
        }

        @Override
        public void presentLoadSettingsFailure(String errorMessage) {
            this.failureMessage = errorMessage;
        }

        public LoadMapSettingsOutputData getSuccessData() {
            return successData;
        }

        public boolean isNoSettingsCalled() {
            return noSettingsCalled;
        }

        public String getFailureMessage() {
            return failureMessage;
        }

        public void reset() {
            successData = null;
            noSettingsCalled = false;
            failureMessage = null;
        }
    }

    // ========== Storage Tests ==========

    @Nested
    class InDiskMapOverlaySettingsStorageTests {
        @Test
        void testHasSavedSettingsWhenFileDoesNotExist(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertFalse(storage.hasSavedSettings());
        }

        @Test
        void testSaveAndHasSavedSettings(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center = new Location(45.5, -73.5);
            storage.save(center, 5, WeatherType.TMP2M);

            assertTrue(storage.hasSavedSettings());
        }

        @Test
        void testGetSavedCenterLocation(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center = new Location(45.5, -73.5);
            storage.save(center, 5, WeatherType.TMP2M);

            Location retrieved = storage.getSavedCenterLocation();
            assertEquals(45.5, retrieved.getLatitude());
            assertEquals(-73.5, retrieved.getLongitude());
        }

        @Test
        void testGetSavedZoomLevel(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center = new Location(45.5, -73.5);
            storage.save(center, 7, WeatherType.PRECIP);

            assertEquals(7, storage.getSavedZoomLevel());
        }

        @Test
        void testGetSavedWeatherType(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center = new Location(45.5, -73.5);
            storage.save(center, 5, WeatherType.PRESSURE);

            assertEquals(WeatherType.PRESSURE, storage.getSavedWeatherType());
        }

        @Test
        void testGetSavedWeatherTypeWhenNull(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center = new Location(45.5, -73.5);
            storage.save(center, 5, null);

            assertNull(storage.getSavedWeatherType());
        }

        @Test
        void testSaveOverwritesPreviousSettings(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center1 = new Location(45.5, -73.5);
            storage.save(center1, 5, WeatherType.TMP2M);

            Location center2 = new Location(46.0, -74.0);
            storage.save(center2, 7, WeatherType.WIND);

            Location retrieved = storage.getSavedCenterLocation();
            assertEquals(46.0, retrieved.getLatitude());
            assertEquals(-74.0, retrieved.getLongitude());
            assertEquals(7, storage.getSavedZoomLevel());
            assertEquals(WeatherType.WIND, storage.getSavedWeatherType());
        }

        @Test
        void testGetSavedCenterLocationThrowsWhenNoSettings(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertThrows(MapOverlaySettingsPersistenceException.class,
                    storage::getSavedCenterLocation);
        }

        @Test
        void testGetSavedZoomLevelThrowsWhenNoSettings(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertThrows(MapOverlaySettingsPersistenceException.class,
                    storage::getSavedZoomLevel);
        }

        @Test
        void testHasSavedSettingsWithCorruptJSON(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("map-settings.json");
            Files.writeString(filePath, "invalid json {");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertFalse(storage.hasSavedSettings());
        }

        @Test
        void testHasSavedSettingsWithIOException(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("map-settings.json");

            Files.createDirectory(filePath);
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertFalse(storage.hasSavedSettings());
        }

        @Test
        void testGetSavedWeatherTypeWithInvalidEnumValue(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("map-settings.json");
            Files.writeString(filePath, "{\"centerLatitude\": 45.0, \"centerLongitude\": -120.0, " +
                    "\"zoomLevel\": 5, \"weatherType\": \"InvalidType\"}");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertNull(storage.getSavedWeatherType());
        }

        @Test
        void testGetSavedCenterLocationWithCorruptJSON(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("map-settings.json");
            Files.writeString(filePath, "invalid json {");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertThrows(MapOverlaySettingsPersistenceException.class,
                    storage::getSavedCenterLocation);
        }

        @Test
        void testGetSavedZoomLevelWithCorruptJSON(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("map-settings.json");
            Files.writeString(filePath, "invalid json {");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertThrows(MapOverlaySettingsPersistenceException.class,
                    storage::getSavedZoomLevel);
        }

        @Test
        void testHasSavedSettingsWithPartialFields(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("map-settings.json");
            Files.writeString(filePath, "{\"centerLatitude\": 45.0}"); 
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertFalse(storage.hasSavedSettings());
        }
    }

    // ========== Save Map Settings Use Case Tests ==========

    @Nested
    class SaveMapSettingsTests {
        @Test
        void testSaveMapSettingsSuccess() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(45.5, -73.5, 5, WeatherType.TMP2M);
            useCase.saveMapSettings(input);

            assertNotNull(presenter.getSuccessData());
            assertTrue(presenter.getSuccessData().isSuccess());
            assertNull(presenter.getFailureMessage());

            assertTrue(storage.hasSavedSettings());
            Location saved = storage.getSavedCenterLocation();
            assertEquals(45.5, saved.getLatitude());
            assertEquals(-73.5, saved.getLongitude());
            assertEquals(5, storage.getSavedZoomLevel());
            assertEquals(WeatherType.TMP2M, storage.getSavedWeatherType());
        }

        @Test
        void testSaveMapSettingsWithNullWeatherType() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(45.5, -73.5, 5, null);
            useCase.saveMapSettings(input);

            assertNotNull(presenter.getSuccessData());
            assertTrue(presenter.getSuccessData().isSuccess());
            assertNull(storage.getSavedWeatherType());
        }

        @Test
        void testSaveMapSettingsWithNullInput() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            useCase.saveMapSettings(null);

            assertNull(presenter.getSuccessData());
            assertEquals("No map settings were provided.", presenter.getFailureMessage());
        }

        @Test
        void testSaveMapSettingsWithNaNLatitude() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(Double.NaN, -73.5, 5, WeatherType.TMP2M);
            useCase.saveMapSettings(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Map center coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testSaveMapSettingsWithNaNLongitude() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(45.5, Double.NaN, 5, WeatherType.TMP2M);
            useCase.saveMapSettings(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Map center coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testSaveMapSettingsWhenStorageFails() {
            FailingMapSettingsStorage storage = new FailingMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(45.5, -73.5, 5, WeatherType.TMP2M);
            useCase.saveMapSettings(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Failed to save map settings.", presenter.getFailureMessage());
        }

        @Test
        void testSaveMapSettingsWithAllWeatherTypes() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            for (WeatherType weatherType : WeatherType.values()) {
                presenter.reset();
                SaveMapSettingsInputData input = new SaveMapSettingsInputData(45.5, -73.5, 5, weatherType);
                useCase.saveMapSettings(input);

                assertNotNull(presenter.getSuccessData());
                assertEquals(weatherType, storage.getSavedWeatherType());
            }
        }

        @Test
        void testSaveMapSettingsWithExtremeCoordinates() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(90.0, 180.0, 10, WeatherType.WIND);
            useCase.saveMapSettings(input);

            assertNotNull(presenter.getSuccessData());
            Location saved = storage.getSavedCenterLocation();
            assertEquals(90.0, saved.getLatitude());
            assertEquals(180.0, saved.getLongitude());
        }
    }

    // ========== Load Map Settings Use Case Tests ==========

    @Nested
    class LoadMapSettingsTests {
        @Test
        void testLoadMapSettingsSuccess() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            Location center = new Location(45.5, -73.5);
            storage.save(center, 7, WeatherType.PRECIP);

            TestLoadMapSettingsPresenter presenter = new TestLoadMapSettingsPresenter();
            LoadMapSettingsUseCase useCase = new LoadMapSettingsUseCase(storage, presenter);

            LoadMapSettingsInputData input = new LoadMapSettingsInputData();
            useCase.loadMapSettings(input);

            assertNotNull(presenter.getSuccessData());
            assertEquals(45.5, presenter.getSuccessData().getCenterLatitude());
            assertEquals(-73.5, presenter.getSuccessData().getCenterLongitude());
            assertEquals(7, presenter.getSuccessData().getZoomLevel());
            assertEquals(WeatherType.PRECIP, presenter.getSuccessData().getWeatherType());
            assertFalse(presenter.isNoSettingsCalled());
            assertNull(presenter.getFailureMessage());
        }

        @Test
        void testLoadMapSettingsWithNullWeatherType() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            Location center = new Location(45.5, -73.5);
            storage.save(center, 5, null);

            TestLoadMapSettingsPresenter presenter = new TestLoadMapSettingsPresenter();
            LoadMapSettingsUseCase useCase = new LoadMapSettingsUseCase(storage, presenter);

            LoadMapSettingsInputData input = new LoadMapSettingsInputData();
            useCase.loadMapSettings(input);

            assertNotNull(presenter.getSuccessData());
            assertNull(presenter.getSuccessData().getWeatherType());
        }

        @Test
        void testLoadMapSettingsWhenNoSettingsExist() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();

            TestLoadMapSettingsPresenter presenter = new TestLoadMapSettingsPresenter();
            LoadMapSettingsUseCase useCase = new LoadMapSettingsUseCase(storage, presenter);

            LoadMapSettingsInputData input = new LoadMapSettingsInputData();
            useCase.loadMapSettings(input);

            assertNull(presenter.getSuccessData());
            assertTrue(presenter.isNoSettingsCalled());
            assertNull(presenter.getFailureMessage());
        }

        @Test
        void testLoadMapSettingsWhenStorageFails() {
            FailingMapSettingsStorage storage = new FailingMapSettingsStorage();
            TestLoadMapSettingsPresenter presenter = new TestLoadMapSettingsPresenter();
            LoadMapSettingsUseCase useCase = new LoadMapSettingsUseCase(storage, presenter);

            LoadMapSettingsInputData input = new LoadMapSettingsInputData();
            useCase.loadMapSettings(input);

            assertNull(presenter.getSuccessData());
            assertFalse(presenter.isNoSettingsCalled());
            assertEquals("Failed to load saved map settings.", presenter.getFailureMessage());
        }

        @Test
        void testLoadMapSettingsWithAllWeatherTypes() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestLoadMapSettingsPresenter presenter = new TestLoadMapSettingsPresenter();
            LoadMapSettingsUseCase useCase = new LoadMapSettingsUseCase(storage, presenter);

            for (WeatherType weatherType : WeatherType.values()) {
                presenter.reset();
                Location center = new Location(45.5, -73.5);
                storage.save(center, 5, weatherType);

                LoadMapSettingsInputData input = new LoadMapSettingsInputData();
                useCase.loadMapSettings(input);

                assertNotNull(presenter.getSuccessData());
                assertEquals(weatherType, presenter.getSuccessData().getWeatherType());
            }
        }

        @Test
        void testLoadMapSettingsWithDifferentZoomLevels() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestLoadMapSettingsPresenter presenter = new TestLoadMapSettingsPresenter();
            LoadMapSettingsUseCase useCase = new LoadMapSettingsUseCase(storage, presenter);

            int[] zoomLevels = {0, 5, 10, 15};
            for (int zoom : zoomLevels) {
                presenter.reset();
                Location center = new Location(45.5, -73.5);
                storage.save(center, zoom, WeatherType.TMP2M);

                LoadMapSettingsInputData input = new LoadMapSettingsInputData();
                useCase.loadMapSettings(input);

                assertNotNull(presenter.getSuccessData());
                assertEquals(zoom, presenter.getSuccessData().getZoomLevel());
            }
        }

        @Test
        void testLoadMapSettingsWithDifferentCoordinates() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestLoadMapSettingsPresenter presenter = new TestLoadMapSettingsPresenter();
            LoadMapSettingsUseCase useCase = new LoadMapSettingsUseCase(storage, presenter);

            double[][] coordinates = {
                    {0.0, 0.0},
                    {45.5, -73.5},
                    {-45.5, 73.5},
                    {90.0, 180.0},
                    {-90.0, -180.0}
            };

            for (double[] coord : coordinates) {
                presenter.reset();
                Location center = new Location(coord[0], coord[1]);
                storage.save(center, 5, WeatherType.PRESSURE);

                LoadMapSettingsInputData input = new LoadMapSettingsInputData();
                useCase.loadMapSettings(input);

                assertNotNull(presenter.getSuccessData());
                assertEquals(coord[0], presenter.getSuccessData().getCenterLatitude());
                assertEquals(coord[1], presenter.getSuccessData().getCenterLongitude());
            }
        }
    }

    // ========== Controller Tests ==========

    @Nested
    class ControllerTests {
        @Test
        void testLoadMapSettingsController() {
            LoadMapSettingsInputBoundary mockUseCase = Mockito.mock(LoadMapSettingsInputBoundary.class);
            LoadMapSettingsController controller = new LoadMapSettingsController(mockUseCase);

            controller.loadMapSettings();

            Mockito.verify(mockUseCase).loadMapSettings(Mockito.any(LoadMapSettingsInputData.class));
        }

        @Test
        void testSaveMapSettingsController() {
            SaveMapSettingsInputBoundary mockUseCase = Mockito.mock(SaveMapSettingsInputBoundary.class);
            SaveMapSettingsController controller = new SaveMapSettingsController(mockUseCase);

            controller.saveMapSettings(45.5, -73.5, 5, WeatherType.TMP2M);

            Mockito.verify(mockUseCase).saveMapSettings(Mockito.argThat(input ->
                    input.getCenterLatitude() == 45.5 &&
                    input.getCenterLongitude() == -73.5 &&
                    input.getZoomLevel() == 5 &&
                    input.getWeatherType() == WeatherType.TMP2M
            ));
        }

        @Test
        void testSaveMapSettingsControllerWithNullWeatherType() {
            SaveMapSettingsInputBoundary mockUseCase = Mockito.mock(SaveMapSettingsInputBoundary.class);
            SaveMapSettingsController controller = new SaveMapSettingsController(mockUseCase);

            controller.saveMapSettings(45.5, -73.5, 5, null);

            Mockito.verify(mockUseCase).saveMapSettings(Mockito.argThat(input ->
                    input.getWeatherType() == null
            ));
        }
    }

    // ========== Presenter Tests ==========

    @Nested
    class PresenterTests {
        @Test
        void testLoadMapSettingsPresenterSuccess() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            LoadMapSettingsPresenter presenter = new LoadMapSettingsPresenter(viewModel);

            LoadMapSettingsOutputData outputData = new LoadMapSettingsOutputData(45.5, -73.5, 5, WeatherType.TMP2M);
            presenter.presentLoadedSettings(outputData);

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertTrue(state.hasSavedSettings());
            assertEquals(45.5, state.getCenterLatitude());
            assertEquals(-73.5, state.getCenterLongitude());
            assertEquals(5, state.getZoomLevel());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testLoadMapSettingsPresenterNoSettings() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            LoadMapSettingsPresenter presenter = new LoadMapSettingsPresenter(viewModel);

            presenter.presentNoSavedSettings();

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertFalse(state.hasSavedSettings());
            assertEquals(0.0, state.getCenterLatitude());
            assertEquals(0.0, state.getCenterLongitude());
            assertEquals(1, state.getZoomLevel());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testLoadMapSettingsPresenterFailure() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            LoadMapSettingsPresenter presenter = new LoadMapSettingsPresenter(viewModel);

            presenter.presentLoadSettingsFailure("Error message");

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testSaveMapSettingsPresenterSuccess() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            viewModel.setSettings(45.5, -73.5, 5);
            SaveMapSettingsPresenter presenter = new SaveMapSettingsPresenter(viewModel);

            SaveMapSettingsOutputData outputData = new SaveMapSettingsOutputData(true);
            presenter.presentSavedSettings(outputData);

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertTrue(state.hasSavedSettings());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testSaveMapSettingsPresenterNotSuccess() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            SaveMapSettingsPresenter presenter = new SaveMapSettingsPresenter(viewModel);

            SaveMapSettingsOutputData outputData = new SaveMapSettingsOutputData(false);
            presenter.presentSavedSettings(outputData);

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertEquals("Saving map settings did not complete successfully.", state.getErrorMessage());
        }

        @Test
        void testSaveMapSettingsPresenterFailure() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            SaveMapSettingsPresenter presenter = new SaveMapSettingsPresenter(viewModel);

            presenter.presentSaveSettingsFailure("Error message");

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testAutoLoadMapSettingsPresenterSuccess() throws entity.LayerNotFoundException {
            Viewport viewport = new Viewport(0.0, 0.0, 800, 1, 15, 0, 600);
            ChangeLayerInputBoundary mockChangeLayer = Mockito.mock(ChangeLayerInputBoundary.class);
            AutoLoadMapSettingsPresenter presenter = new AutoLoadMapSettingsPresenter(viewport, mockChangeLayer);

            LoadMapSettingsOutputData outputData = new LoadMapSettingsOutputData(
                    45.5, -73.5, 5, entity.WeatherType.TMP2M);
            presenter.presentLoadedSettings(outputData);

            assertEquals(5, viewport.getZoomLevel());
            Mockito.verify(mockChangeLayer).change(Mockito.any(ChangeLayerInputData.class));
        }

        @Test
        void testAutoLoadMapSettingsPresenterWithNullWeatherType() throws entity.LayerNotFoundException {
            Viewport viewport = new Viewport(0.0, 0.0, 800, 1, 15, 0, 600);
            ChangeLayerInputBoundary mockChangeLayer = Mockito.mock(ChangeLayerInputBoundary.class);
            AutoLoadMapSettingsPresenter presenter = new AutoLoadMapSettingsPresenter(viewport, mockChangeLayer);

            LoadMapSettingsOutputData outputData = new LoadMapSettingsOutputData(45.5, -73.5, 5, null);
            presenter.presentLoadedSettings(outputData);

            assertEquals(5, viewport.getZoomLevel());
            Mockito.verify(mockChangeLayer, Mockito.never()).change(Mockito.any());
        }

        @Test
        void testAutoLoadMapSettingsPresenterWithLayerNotFoundException() throws entity.LayerNotFoundException {
            Viewport viewport = new Viewport(0.0, 0.0, 800, 1, 15, 0, 600);
            ChangeLayerInputBoundary mockChangeLayer = Mockito.mock(ChangeLayerInputBoundary.class);
            Mockito.doThrow(new entity.LayerNotFoundException("Layer not found"))
                    .when(mockChangeLayer).change(Mockito.any(ChangeLayerInputData.class));
            AutoLoadMapSettingsPresenter presenter = new AutoLoadMapSettingsPresenter(viewport, mockChangeLayer);

            LoadMapSettingsOutputData outputData = new LoadMapSettingsOutputData(
                    45.5, -73.5, 5, entity.WeatherType.TMP2M);

            presenter.presentLoadedSettings(outputData);

            assertEquals(5, viewport.getZoomLevel());
        }

        @Test
        void testAutoLoadMapSettingsPresenterNoSettings() throws entity.LayerNotFoundException {
            Viewport viewport = new Viewport(0.0, 0.0, 800, 1, 15, 0, 600);
            ChangeLayerInputBoundary mockChangeLayer = Mockito.mock(ChangeLayerInputBoundary.class);
            AutoLoadMapSettingsPresenter presenter = new AutoLoadMapSettingsPresenter(viewport, mockChangeLayer);

            presenter.presentNoSavedSettings();

            assertEquals(1, viewport.getZoomLevel());
            Mockito.verify(mockChangeLayer, Mockito.never()).change(Mockito.any());
        }

        @Test
        void testAutoLoadMapSettingsPresenterFailure() throws entity.LayerNotFoundException {
            Viewport viewport = new Viewport(0.0, 0.0, 800, 1, 15, 0, 600);
            ChangeLayerInputBoundary mockChangeLayer = Mockito.mock(ChangeLayerInputBoundary.class);
            AutoLoadMapSettingsPresenter presenter = new AutoLoadMapSettingsPresenter(viewport, mockChangeLayer);

            presenter.presentLoadSettingsFailure("Error");

            assertEquals(1, viewport.getZoomLevel());
            Mockito.verify(mockChangeLayer, Mockito.never()).change(Mockito.any());
        }
    }

    // ========== ViewModel Tests ==========

    @Nested
    class ViewModelTests {
        @Test
        void testMapSettingsViewModelInitialization() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertNotNull(state);
            assertFalse(state.hasSavedSettings());
            assertEquals(0.0, state.getCenterLatitude());
            assertEquals(0.0, state.getCenterLongitude());
            assertEquals(0, state.getZoomLevel());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testMapSettingsViewModelSetSettings() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            viewModel.setSettings(45.5, -73.5, 5);

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertTrue(state.hasSavedSettings());
            assertEquals(45.5, state.getCenterLatitude());
            assertEquals(-73.5, state.getCenterLongitude());
            assertEquals(5, state.getZoomLevel());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testMapSettingsViewModelSetError() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            viewModel.setError("Error message");

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testMapSettingsViewModelSetErrorPreservesSettings() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            viewModel.setSettings(45.5, -73.5, 5);
            viewModel.setError("Error message");

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertTrue(state.hasSavedSettings());
            assertEquals(45.5, state.getCenterLatitude());
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testMapSettingsViewModelSetState() {
            MapSettingsViewModel viewModel = new MapSettingsViewModel();
            MapSettingsViewModel.MapSettingsState newState = new MapSettingsViewModel.MapSettingsState(
                    true, 45.5, -73.5, 5, null
            );
            viewModel.setState(newState);

            MapSettingsViewModel.MapSettingsState state = viewModel.getState();
            assertTrue(state.hasSavedSettings());
            assertEquals(45.5, state.getCenterLatitude());
        }

        @Test
        void testMapSettingsStateGetters() {
            MapSettingsViewModel.MapSettingsState state = new MapSettingsViewModel.MapSettingsState(
                    true, 45.5, -73.5, 5, "Error"
            );

            assertTrue(state.hasSavedSettings());
            assertEquals(45.5, state.getCenterLatitude());
            assertEquals(-73.5, state.getCenterLongitude());
            assertEquals(5, state.getZoomLevel());
            assertEquals("Error", state.getErrorMessage());
        }
    }

    // ========== Additional Storage Edge Cases ==========

    @Nested
    class AdditionalStorageTests {
        @ParameterizedTest
        @CsvSource({
                "'{\"centerLongitude\": -120.0, \"zoomLevel\": 5}', 'Missing centerLatitude'",
                "'{\"centerLatitude\": 45.0, \"zoomLevel\": 5}', 'Missing centerLongitude'",
                "'{\"centerLatitude\": 45.0, \"centerLongitude\": -120.0}', 'Missing zoomLevel'"
        })
        void testHasSavedSettingsWithMissingFields(
                String jsonContent, String description, @TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("map-settings.json");
            Files.writeString(filePath, jsonContent);
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertFalse(storage.hasSavedSettings(), description);
        }

        @Test
        void testGetSavedWeatherTypeWhenNotPresent(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("map-settings.json");
            Files.writeString(filePath, "{\"centerLatitude\": 45.0, \"centerLongitude\": -120.0, \"zoomLevel\": 5}");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertNull(storage.getSavedWeatherType());
        }
    }
}


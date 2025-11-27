package usecase.mapsettings;

import dataaccessinterface.SavedMapOverlaySettings;
import dataaccessobjects.InDiskMapOverlaySettingsStorage;
import dataaccessobjects.MapOverlaySettingsPersistenceException;
import entity.Location;
import entity.WeatherType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import usecase.mapsettings.loadmapsettings.*;
import usecase.mapsettings.savemapsettings.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
            storage.save(center, 5, WeatherType.Tmp2m);

            assertTrue(storage.hasSavedSettings());
        }

        @Test
        void testGetSavedCenterLocation(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center = new Location(45.5, -73.5);
            storage.save(center, 5, WeatherType.Tmp2m);

            Location retrieved = storage.getSavedCenterLocation();
            assertEquals(45.5, retrieved.getLatitude());
            assertEquals(-73.5, retrieved.getLongitude());
        }

        @Test
        void testGetSavedZoomLevel(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center = new Location(45.5, -73.5);
            storage.save(center, 7, WeatherType.Precip);

            assertEquals(7, storage.getSavedZoomLevel());
        }

        @Test
        void testGetSavedWeatherType(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            Location center = new Location(45.5, -73.5);
            storage.save(center, 5, WeatherType.Pressure);

            assertEquals(WeatherType.Pressure, storage.getSavedWeatherType());
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
            storage.save(center1, 5, WeatherType.Tmp2m);

            Location center2 = new Location(46.0, -74.0);
            storage.save(center2, 7, WeatherType.Wind);

            Location retrieved = storage.getSavedCenterLocation();
            assertEquals(46.0, retrieved.getLatitude());
            assertEquals(-74.0, retrieved.getLongitude());
            assertEquals(7, storage.getSavedZoomLevel());
            assertEquals(WeatherType.Wind, storage.getSavedWeatherType());
        }

        @Test
        void testGetSavedCenterLocationThrowsWhenNoSettings(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertThrows(MapOverlaySettingsPersistenceException.class,
                    () -> storage.getSavedCenterLocation());
        }

        @Test
        void testGetSavedZoomLevelThrowsWhenNoSettings(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("map-settings.json");
            InDiskMapOverlaySettingsStorage storage = new InDiskMapOverlaySettingsStorage(filePath);

            assertThrows(MapOverlaySettingsPersistenceException.class,
                    () -> storage.getSavedZoomLevel());
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

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(45.5, -73.5, 5, WeatherType.Tmp2m);
            useCase.saveMapSettings(input);

            assertNotNull(presenter.getSuccessData());
            assertTrue(presenter.getSuccessData().isSuccess());
            assertNull(presenter.getFailureMessage());

            assertTrue(storage.hasSavedSettings());
            Location saved = storage.getSavedCenterLocation();
            assertEquals(45.5, saved.getLatitude());
            assertEquals(-73.5, saved.getLongitude());
            assertEquals(5, storage.getSavedZoomLevel());
            assertEquals(WeatherType.Tmp2m, storage.getSavedWeatherType());
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

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(Double.NaN, -73.5, 5, WeatherType.Tmp2m);
            useCase.saveMapSettings(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Map center coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testSaveMapSettingsWithNaNLongitude() {
            InMemoryMapSettingsStorage storage = new InMemoryMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(45.5, Double.NaN, 5, WeatherType.Tmp2m);
            useCase.saveMapSettings(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Map center coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testSaveMapSettingsWhenStorageFails() {
            FailingMapSettingsStorage storage = new FailingMapSettingsStorage();
            TestSaveMapSettingsPresenter presenter = new TestSaveMapSettingsPresenter();
            SaveMapSettingsUseCase useCase = new SaveMapSettingsUseCase(storage, presenter);

            SaveMapSettingsInputData input = new SaveMapSettingsInputData(45.5, -73.5, 5, WeatherType.Tmp2m);
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

            // Test with valid extreme coordinates
            SaveMapSettingsInputData input = new SaveMapSettingsInputData(90.0, 180.0, 10, WeatherType.Wind);
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
            storage.save(center, 7, WeatherType.Precip);

            TestLoadMapSettingsPresenter presenter = new TestLoadMapSettingsPresenter();
            LoadMapSettingsUseCase useCase = new LoadMapSettingsUseCase(storage, presenter);

            LoadMapSettingsInputData input = new LoadMapSettingsInputData();
            useCase.loadMapSettings(input);

            assertNotNull(presenter.getSuccessData());
            assertEquals(45.5, presenter.getSuccessData().getCenterLatitude());
            assertEquals(-73.5, presenter.getSuccessData().getCenterLongitude());
            assertEquals(7, presenter.getSuccessData().getZoomLevel());
            assertEquals(WeatherType.Precip, presenter.getSuccessData().getWeatherType());
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
            // Don't save anything

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
                storage.save(center, zoom, WeatherType.Tmp2m);

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
                storage.save(center, 5, WeatherType.Pressure);

                LoadMapSettingsInputData input = new LoadMapSettingsInputData();
                useCase.loadMapSettings(input);

                assertNotNull(presenter.getSuccessData());
                assertEquals(coord[0], presenter.getSuccessData().getCenterLatitude());
                assertEquals(coord[1], presenter.getSuccessData().getCenterLongitude());
            }
        }
    }
}


import interfaceadapter.infopanel.InfoPanelController;
import interfaceadapter.infopanel.InfoPanelPresenter;
import interfaceadapter.infopanel.InfoPanelViewModel;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import usecase.infopanel.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InfoPanelUseCaseTest {

    // ======= Test doubles =======

    /**
     * Simple PointWeatherFetcher test double that returns configurable XML
     * or throws IOException on demand.
     */
    private static class FakeFetcher implements PointWeatherFetcher {
        private String xml;
        private boolean throwIo;
        double lastLat;
        double lastLon;

        void setXml(String xml) {
            this.xml = xml;
            this.throwIo = false;
        }

        void setThrowIo(boolean throwIo) {
            this.throwIo = throwIo;
        }

        @Override
        public String fetch(double lat, double lon) throws IOException {
            lastLat = lat;
            lastLon = lon;
            if (throwIo) {
                throw new IOException("boom");
            }
            return xml;
        }
    }

    /**
     * Test double for InfoPanelOutputBoundary.
     */
    private static class TestOutputPresenter implements InfoPanelOutputBoundary {
        int loadingCount;
        InfoPanelOutputData lastData;
        InfoPanelError lastError;

        @Override
        public void presentLoading() {
            loadingCount++;
        }

        @Override
        public void present(InfoPanelOutputData data) {
            lastData = data;
        }

        @Override
        public void presentError(InfoPanelError error) {
            lastError = error;
        }

        void reset() {
            loadingCount = 0;
            lastData = null;
            lastError = null;
        }
    }

    /**
     * Test double for InfoPanelInputBoundary.
     */
    private static class TestInputBoundary implements InfoPanelInputBoundary {
        InfoPanelInputData lastRequest;

        @Override
        public void execute(InfoPanelInputData req) {
            lastRequest = req;
        }
    }

    /**
     * ChangeListener that just counts notifications.
     */
    private static class CountingListener implements ChangeListener {
        int count;

        @Override
        public void stateChanged(ChangeEvent e) {
            count++;
        }
    }

    // ======= Interactor tests =======

    @Nested
    class InteractorTests {

        @Test
        void execute_success_parsesAllFields() {
            // language=XML
            String xml = """
                    <root>
                      <location>
                        <name>Toronto</name>
                      </location>
                      <current>
                        <temp_c>12.3</temp_c>
                        <condition>
                          <text>Cloudy</text>
                        </condition>
                        <humidity>70</humidity>
                        <wind_kph>15.5</wind_kph>
                        <wind_dir>NW</wind_dir>
                        <pressure_mb>1012</pressure_mb>
                        <precip_mm>0.5</precip_mm>
                        <feelslike_c>10.0</feelslike_c>
                        <uv>3</uv>
                        <cloud>80</cloud>
                      </current>
                      <forecast>
                        <forecastday>
                          <hour><temp_c>1.0</temp_c></hour>
                          <hour><temp_c>2.0</temp_c></hour>
                        </forecastday>
                      </forecast>
                    </root>
                    """;

            FakeFetcher fetcher = new FakeFetcher();
            fetcher.setXml(xml);
            TestOutputPresenter out = new TestOutputPresenter();
            InfoPanelInteractor interactor = new InfoPanelInteractor(fetcher, out);

            InfoPanelInputData req = new InfoPanelInputData(43.7, -79.4, 10);
            interactor.execute(req);

            assertNull(out.lastError);
            assertNotNull(out.lastData);

            InfoPanelOutputData data = out.lastData;
            assertEquals("Toronto", data.getPlaceName());
            assertEquals(12.3, data.getTempC(), 1e-6);
            assertEquals("Cloudy", data.getCondition());

            List<Double> temps = data.getHourlyTemps();
            assertEquals(2, temps.size());
            assertEquals(1.0, temps.get(0), 1e-6);
            assertEquals(2.0, temps.get(1), 1e-6);

            Map<String, String> extra = data.getExtra();
            assertEquals("70", extra.get(InfoPanelViewModel.EXTRA_HUMIDITY));
            assertEquals("15.5", extra.get(InfoPanelViewModel.EXTRA_WIND_KPH));
            assertEquals("NW", extra.get(InfoPanelViewModel.EXTRA_WIND_DIR));
            assertEquals("1012", extra.get(InfoPanelViewModel.EXTRA_PRESSURE_MB));
            assertEquals("0.5", extra.get(InfoPanelViewModel.EXTRA_PRECIP_MM));
            assertEquals("10.0", extra.get(InfoPanelViewModel.EXTRA_FEELSLIKE_C));
            assertEquals("3", extra.get(InfoPanelViewModel.EXTRA_UV));
            assertEquals("80", extra.get(InfoPanelViewModel.EXTRA_CLOUD));

            assertNotNull(data.getFetchedAt());
        }

        @Test
        void execute_missingSections_usesDefaultsAndNulls() {
            String xml = "<root></root>";

            FakeFetcher fetcher = new FakeFetcher();
            fetcher.setXml(xml);
            TestOutputPresenter out = new TestOutputPresenter();
            InfoPanelInteractor interactor = new InfoPanelInteractor(fetcher, out);

            interactor.execute(new InfoPanelInputData(0, 0, 1));

            assertNull(out.lastError);
            assertNotNull(out.lastData);

            InfoPanelOutputData data = out.lastData;
            assertEquals(InfoPanelViewModel.TAG_LOCATION, data.getPlaceName());
            assertNull(data.getTempC());
            assertNull(data.getCondition());
            assertTrue(data.getHourlyTemps().isEmpty());
            assertTrue(data.getExtra().isEmpty());
        }

        @Test
        void execute_invalidNumericFields_turnsIntoNull() {
            String xml = """
                    <root>
                      <current>
                        <temp_c>not_a_number</temp_c>
                      </current>
                    </root>
                    """;

            FakeFetcher fetcher = new FakeFetcher();
            fetcher.setXml(xml);
            TestOutputPresenter out = new TestOutputPresenter();
            InfoPanelInteractor interactor = new InfoPanelInteractor(fetcher, out);

            interactor.execute(new InfoPanelInputData(0, 0, 1));

            assertNotNull(out.lastData);
            assertNull(out.lastData.getTempC());
        }

        @Test
        void execute_fetcherThrowsIOException_presentsFetchFailed() {
            FakeFetcher fetcher = new FakeFetcher();
            fetcher.setThrowIo(true);
            TestOutputPresenter out = new TestOutputPresenter();
            InfoPanelInteractor interactor = new InfoPanelInteractor(fetcher, out);

            interactor.execute(new InfoPanelInputData(1, 2, 3));

            assertEquals(InfoPanelError.FETCH_FAILED, out.lastError);
            assertNull(out.lastData);
        }

        @Test
        void execute_malformedXml_presentsFetchFailed() {
            String xml = "<root><unclosed-tag>";

            FakeFetcher fetcher = new FakeFetcher();
            fetcher.setXml(xml);
            TestOutputPresenter out = new TestOutputPresenter();
            InfoPanelInteractor interactor = new InfoPanelInteractor(fetcher, out);

            interactor.execute(new InfoPanelInputData(1, 2, 3));

            assertEquals(InfoPanelError.FETCH_FAILED, out.lastError);
            assertNull(out.lastData);
        }
    }

    // ======= Controller tests =======

    @Nested
    class ControllerTests {

        private void invokeFireIfTargetChanged(InfoPanelController controller) throws Exception {
            var m = InfoPanelController.class.getDeclaredMethod("fireIfTargetChanged");
            m.setAccessible(true);
            m.invoke(controller);
        }

        @Test
        void onViewportChanged_triggersLoadingAndInteractorCall() throws Exception {
            TestInputBoundary interactor = new TestInputBoundary();
            TestOutputPresenter presenter = new TestOutputPresenter();
            InfoPanelController controller = new InfoPanelController(interactor, presenter);

            controller.onViewportChanged(10.0, 20.0, 5);

            assertEquals(1, presenter.loadingCount);

            invokeFireIfTargetChanged(controller);

            assertNotNull(interactor.lastRequest);
            assertEquals(10.0, interactor.lastRequest.getCenterLat(), 1e-6);
            assertEquals(20.0, interactor.lastRequest.getCenterLon(), 1e-6);
            assertEquals(5, interactor.lastRequest.getZoom());
        }

        @Test
        void closeAndRevisitSameTile_resultsInUserClosedError() throws Exception {
            TestInputBoundary interactor = new TestInputBoundary();
            TestOutputPresenter presenter = new TestOutputPresenter();
            InfoPanelController controller = new InfoPanelController(interactor, presenter);

            double lat = 43.7;
            double lon = -79.4;
            int zoom = 8;

            controller.onViewportChanged(lat, lon, zoom);
            invokeFireIfTargetChanged(controller);

            presenter.reset();
            controller.onCloseRequested();
            assertEquals(InfoPanelError.USER_CLOSED, presenter.lastError);

            presenter.reset();
            controller.onViewportChanged(lat, lon, zoom);
            assertEquals(InfoPanelError.USER_CLOSED, presenter.lastError);

            presenter.reset();
            invokeFireIfTargetChanged(controller);
            assertEquals(InfoPanelError.USER_CLOSED, presenter.lastError);
        }
    }

    // ======= Presenter tests =======

    @Nested
    class PresenterTests {

        @Test
        void addChangeListener_ignoresNull() {
            InfoPanelViewModel vm = new InfoPanelViewModel();
            InfoPanelPresenter presenter = new InfoPanelPresenter(vm);

            presenter.addChangeListener(null);
        }

        @Test
        void presentLoading_setsLoadingAndVisible_andNotifies() {
            InfoPanelViewModel vm = new InfoPanelViewModel();
            InfoPanelPresenter presenter = new InfoPanelPresenter(vm);
            CountingListener listener = new CountingListener();
            presenter.addChangeListener(listener);

            presenter.presentLoading();

            assertTrue(vm.isLoading());
            assertTrue(vm.getVisible());
            assertNull(vm.getError());
            assertEquals(1, listener.count);
        }

        @Test
        void present_setsDataAndClearsError_andNotifies() {
            InfoPanelViewModel vm = new InfoPanelViewModel();
            InfoPanelPresenter presenter = new InfoPanelPresenter(vm);
            CountingListener listener = new CountingListener();
            presenter.addChangeListener(listener);

            List<Double> temps = Arrays.asList(1.0, 2.0);
            Map<String, String> extra = Map.of("humidity", "70");
            Instant now = Instant.now();
            InfoPanelOutputData data = new InfoPanelOutputData(
                    "Toronto", 10.5, "Sunny", temps, now, extra);

            presenter.present(data);

            assertFalse(vm.isLoading());
            assertTrue(vm.getVisible());
            assertNull(vm.getError());
            assertEquals("Toronto", vm.getPlaceName());
            assertEquals(10.5, vm.getTempC(), 1e-6);
            assertEquals("Sunny", vm.getCondition());
            assertEquals(temps, vm.getHourlyTemps());
            assertEquals(now, vm.getFetchedAt());
            assertEquals(1, listener.count);
        }

        @Test
        void presentError_hiddenByZoom_hidesAndClearsData() {
            InfoPanelViewModel vm = new InfoPanelViewModel();
            InfoPanelPresenter presenter = new InfoPanelPresenter(vm);

            vm.setVisible(true);
            vm.setPlaceName("Something");
            vm.setTempC(5.0);
            vm.setCondition("Cloudy");
            vm.setHourlyTemps(Arrays.asList(1.0, 2.0));
            vm.setFetchedAt(Instant.now());

            presenter.presentError(InfoPanelError.HIDDEN_BY_ZOOM);

            assertFalse(vm.getVisible());
            assertEquals(InfoPanelError.HIDDEN_BY_ZOOM, vm.getError());
            assertNull(vm.getPlaceName());
            assertNull(vm.getTempC());
            assertNull(vm.getCondition());
            assertNotNull(vm.getHourlyTemps());
            assertTrue(vm.getHourlyTemps().isEmpty());
            assertNull(vm.getFetchedAt());
        }

        @Test
        void presentError_fetchFailed_keepsVisibleAndDoesNotClearData() {
            InfoPanelViewModel vm = new InfoPanelViewModel();
            InfoPanelPresenter presenter = new InfoPanelPresenter(vm);

            vm.setVisible(true);
            vm.setPlaceName("Toronto");
            vm.setTempC(3.0);
            vm.setCondition("Cloudy");

            presenter.presentError(InfoPanelError.FETCH_FAILED);

            assertTrue(vm.getVisible());
            assertEquals(InfoPanelError.FETCH_FAILED, vm.getError());
            assertEquals("Toronto", vm.getPlaceName());
            assertEquals(3.0, vm.getTempC(), 1e-6);
            assertEquals("Cloudy", vm.getCondition());
        }
    }

    // ======= ViewModel tests =======

    @Nested
    class ViewModelTests {

        @Test
        void initialState_isEmptyAndNotVisible() {
            InfoPanelViewModel vm = new InfoPanelViewModel();

            assertFalse(vm.getVisible());
            assertFalse(vm.isLoading());
            assertNull(vm.getError());
            assertNull(vm.getPlaceName());
            assertNull(vm.getTempC());
            assertNull(vm.getCondition());
            assertNull(vm.getHourlyTemps());
            assertNull(vm.getFetchedAt());
        }

        @Test
        void setters_updateFields() {
            InfoPanelViewModel vm = new InfoPanelViewModel();
            Instant now = Instant.now();
            List<Double> temps = Arrays.asList(1.0, 2.0);

            vm.setVisible(true);
            vm.setLoading(true);
            vm.setError(InfoPanelError.FETCH_FAILED);
            vm.setPlaceName("Toronto");
            vm.setTempC(12.3);
            vm.setCondition("Sunny");
            vm.setHourlyTemps(temps);
            vm.setFetchedAt(now);

            assertTrue(vm.getVisible());
            assertTrue(vm.isLoading());
            assertEquals(InfoPanelError.FETCH_FAILED, vm.getError());
            assertEquals("Toronto", vm.getPlaceName());
            assertEquals(12.3, vm.getTempC(), 1e-6);
            assertEquals("Sunny", vm.getCondition());
            assertEquals(temps, vm.getHourlyTemps());
            assertEquals(now, vm.getFetchedAt());
        }
    }

    // ======= Input / Output data tests =======

    @Nested
    class DataClassesTests {

        @Test
        void infoPanelInputData_gettersWork() {
            InfoPanelInputData data = new InfoPanelInputData(1.0, 2.0, 5);
            assertEquals(1.0, data.getCenterLat(), 1e-6);
            assertEquals(2.0, data.getCenterLon(), 1e-6);
            assertEquals(5, data.getZoom());
        }

        @Test
        void infoPanelOutputData_gettersWork() {
            List<Double> temps = Arrays.asList(1.0, 2.0);
            Instant now = Instant.now();
            Map<String, String> extra = Map.of("k", "v");

            InfoPanelOutputData data = new InfoPanelOutputData(
                    "Place", 3.0, "Cond", temps, now, extra);

            assertEquals("Place", data.getPlaceName());
            assertEquals(3.0, data.getTempC(), 1e-6);
            assertEquals("Cond", data.getCondition());
            assertEquals(temps, data.getHourlyTemps());
            assertEquals(now, data.getFetchedAt());
            assertEquals(extra, data.getExtra());
        }
    }
}

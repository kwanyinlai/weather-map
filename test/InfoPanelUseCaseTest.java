package usecase.infopanel;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfoPanelInteractorTest {

    static final class FakeFetcher implements PointWeatherFetcher {
        final String xml;
        final RuntimeException toThrow;

        FakeFetcher(String xml) {
            this.xml = xml;
            this.toThrow = null;
        }

        FakeFetcher(RuntimeException ex) {
            this.xml = null;
            this.toThrow = ex;
        }

        @Override
        public String fetch(double lat, double lon) {
            if (toThrow != null) throw toThrow;
            return xml;
        }
    }

    private static String sampleWeatherXml() {
        return ""
                + "<root>"
                + "  <location><name>Mirabel</name></location>"
                + "  <current>"
                + "    <temp_c>2.3</temp_c>"
                + "    <condition><text>Partly cloudy</text></condition>"
                + "  </current>"
                + "  <forecast>"
                + "    <forecastday>"
                + "      <hour><time_epoch>1700000000</time_epoch><temp_c>-2</temp_c></hour>"
                + "      <hour><time_epoch>1700003600</time_epoch><temp_c>-2</temp_c></hour>"
                + "      <hour><time_epoch>1700007200</time_epoch><temp_c>-1</temp_c></hour>"
                + "      <hour><time_epoch>1700010800</time_epoch><temp_c>-1</temp_c></hour>"
                + "      <hour><time_epoch>1700014400</time_epoch><temp_c>-1</temp_c></hour>"
                + "    </forecastday>"
                + "  </forecast>"
                + "</root>";
    }

    @Test
    void execute_success_parsesXml_andNotifiesPresenterOnce() {
        PointWeatherFetcher fetcher = new FakeFetcher(sampleWeatherXml());

        List<String> calls = new ArrayList<>();

        InfoPanelOutputBoundary presenter = new InfoPanelOutputBoundary() {
            @Override
            public void presentLoading() {
                calls.add("loading");
            }

            @Override
            public void present(InfoPanelOutputData out) {
                calls.add("present");

                assertEquals("Mirabel", out.placeName, "City name");
                assertEquals(2.3, out.tempC, 1e-6, "Current temp (°C)");
                assertEquals("Partly cloudy", out.condition, "Condition text");

                assertNotNull(out.fetchedAt, "Fetched timestamp");

                assertTrue(out.fetchedAt.isBefore(Instant.now().plusSeconds(3)));

                assertNotNull(out.hourlyTemps, "Hourly list present");
                assertTrue(out.hourlyTemps.size() >= 3, "At least 3 hours parsed");

                assertTrue(out.hourlyTemps.contains(-2.0) || out.hourlyTemps.contains(-1.0));
            }

            @Override
            public void presentError(InfoPanelError error) {
                fail("Unexpected error: " + error);
            }
        };

        InfoPanelInteractor interactor = new InfoPanelInteractor(fetcher, presenter);

        interactor.execute(new InfoPanelInputData(45.65, -73.75, 10));

        assertEquals(Arrays.asList("loading", "present"), calls, "Presenter call order");
    }

    @Test
    void execute_fetcherThrows_reportsErrorAndDoesNotPresent() {
        PointWeatherFetcher fetcher = new FakeFetcher(new RuntimeException("boom"));

        final boolean[] sawLoading = {false};
        final boolean[] sawError   = {false};
        final boolean[] sawPresent = {false};

        InfoPanelOutputBoundary presenter = new InfoPanelOutputBoundary() {
            @Override
            public void presentLoading() {
                sawLoading[0] = true;
            }

            @Override
            public void present(InfoPanelOutputData out) {
                sawPresent[0] = true;
                fail("present(...) should not be called on fetch failure");
            }

            @Override
            public void presentError(InfoPanelError error) {
                assertNotNull(error, "Error enum should be provided");
                sawError[0] = true;
            }
        };

        InfoPanelInteractor interactor = new InfoPanelInteractor(fetcher, presenter);

        interactor.execute(new InfoPanelInputData(0.0, 0.0, 8));

        assertTrue(sawLoading[0], "presentLoading() should be called first");
        assertTrue(sawError[0],   "presentError() should be called on failure");
        assertFalse(sawPresent[0], "present(...) must not be called after failure");
    }
}

package dataaccessinterface;

import java.io.IOException;

public interface PointWeatherFetcher {
    /**
     * Fetches weather data for the given latitude and longitude.
     *
     * @param lat latitude in degrees
     * @param lon longitude in degrees
     * @return raw weather data (for example JSON or XML) as a string
     * @throws IOException if the fetch fails
     */
    String fetch(double lat, double lon) throws IOException;
}

package dataaccessobjects;

import java.io.IOException;

import dataaccessinterface.PointWeatherFetcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpsPointWeatherGatewayXml implements PointWeatherFetcher {

    private final OkHttpClient http = new OkHttpClient();
    private final String apiKey;

    public OkHttpsPointWeatherGatewayXml() {
        final String loadedApiKey = System.getenv("WEATHER_API_KEY");
        if (loadedApiKey == null || loadedApiKey.isBlank()) {
            throw new IllegalArgumentException("apiKey is blank");
        }
        this.apiKey = loadedApiKey;
    }

    public OkHttpsPointWeatherGatewayXml(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("apiKey is blank");
        }
        this.apiKey = apiKey;
    }

    @Override
    public String fetch(double lat, double lon) throws IOException {
        final String url = String.format(
                "https://api.weatherapi.com/v1/forecast.xml?key=%s&q=%f,%f&days=1&aqi=no&alerts=no",
                apiKey, lat, lon
        );
        final Request req = new Request.Builder().url(url).build();

        try (Response resp = http.newCall(req).execute()) {
            if (!resp.isSuccessful() || resp.body() == null) {
                throw new IOException("WeatherAPI HTTP failed: " + resp.code());
            }
            return resp.body().string();
        }
    }
}

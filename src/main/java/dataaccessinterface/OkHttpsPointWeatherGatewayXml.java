package dataaccessinterface;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import usecase.infopanel.PointWeatherFetcher;

public class OkHttpsPointWeatherGatewayXml implements PointWeatherFetcher {

    private final OkHttpClient http = new OkHttpClient();
    private final String apiKey;

    public OkHttpsPointWeatherGatewayXml() {
        String k = System.getProperty("WEATHERAPI_KEY");
        if (k == null || k.isBlank()) k = System.getenv("WEATHERAPI_KEY");
        if (k == null || k.isBlank())
            throw new IllegalStateException("WEATHERAPI_KEY not set");
        this.apiKey = k;
    }

    public OkHttpsPointWeatherGatewayXml(String apiKey) {
        if (apiKey == null || apiKey.isBlank())
            throw new IllegalArgumentException("apiKey is blank");
        this.apiKey = apiKey;
    }

    @Override
    public String fetch(double lat, double lon) throws Exception {
        String url = String.format(
                "https://api.weatherapi.com/v1/forecast.xml?key=%s&q=%f,%f&days=1&aqi=no&alerts=no",
                apiKey, lat, lon
        );
        Request req = new Request.Builder().url(url).build();
        try (Response resp = http.newCall(req).execute()) {
            if (!resp.isSuccessful() || resp.body() == null) {
                throw new RuntimeException("WeatherAPI HTTP failed: " + resp.code());
            }
            return resp.body().string();
        }
    }
}

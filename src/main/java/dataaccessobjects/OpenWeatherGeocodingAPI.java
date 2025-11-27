package dataaccessobjects;

import dataaccessinterface.GeocodingAPI;
import entity.LocationWithName;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherGeocodingAPI implements GeocodingAPI {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://api.openweathermap.org/geo/1.0/direct";

    @Override
    public List<LocationWithName> geocode(String query) {
        List<LocationWithName> results = new ArrayList<>();
        String encodedQuery = query.replace(" ", "%20");
        String geocodingKey = System.getenv("GEOCODING_KEY");
        String url = String.format("%s?q=%s&limit=5&appid=%s", API_URL, encodedQuery, geocodingKey);

        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                JSONArray jsonArray = new JSONArray(response.body().string());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String name = json.getString("name") + ", " + json.getString("country");
                    double lat = json.getDouble("lat");
                    double lon = json.getDouble("lon");
                    results.add(new LocationWithName(name, lat, lon));
                }
            }
        } catch (IOException ignored){//this exception can't be ignored

    }
        return results;
    }
}

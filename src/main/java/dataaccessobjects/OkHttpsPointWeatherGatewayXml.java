package dataaccessobjects;

import dataaccessinterface.WeatherGateway;
import entity.WeatherData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;

public class OkHttpsPointWeatherGatewayXml implements WeatherGateway {

    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    public OkHttpsPointWeatherGatewayXml() {
        String k = System.getenv("WEATHERAPI_KEY");
        if (k == null) k = System.getProperty("WEATHERAPI_KEY");
        if (k == null || k.isBlank()) throw new IllegalStateException("WEATHERAPI_KEY not set");
        this.apiKey = k;
    }

    @Override
    public WeatherData fetchCurrentAndHourly(double lat, double lon) throws Exception {
        String url = String.format(
                java.util.Locale.US,
                "https://api.weatherapi.com/v1/forecast.xml?key=%s&q=%f,%f&hours=12&aqi=no&alerts=no",
                apiKey, lat, lon
        );

        Request req = new Request.Builder().url(url).build();
        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful() || resp.body() == null) {
                throw new RuntimeException("WeatherAPI HTTP failed: " + resp.code());
            }

            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(resp.body().byteStream());
            doc.getDocumentElement().normalize();

            String placeName = String.format(java.util.Locale.US, "Lat %.3f, Lon %.3f", lat, lon);
            NodeList locList = doc.getElementsByTagName("location");
            if (locList.getLength() > 0) {
                Element loc = (Element) locList.item(0);
                String name = text(loc, "name");
                if (name != null && !name.isBlank()) placeName = name;
            }

            Map<String, Object> current = new HashMap<>();
            NodeList curList = doc.getElementsByTagName("current");
            if (curList.getLength() > 0) {
                Element cur = (Element) curList.item(0);
                current.put("tempC",      parseDouble(text(cur, "temp_c")));
                current.put("humidity",   parseDouble(text(cur, "humidity")));
                current.put("windKph",    parseDouble(text(cur, "wind_kph")));
                current.put("feelsLikeC", parseDouble(text(cur, "feelslike_c")));
                NodeList condList = cur.getElementsByTagName("condition");
                if (condList.getLength() > 0) {
                    Element cond = (Element) condList.item(0);
                    current.put("condition", text(cond, "text"));
                    current.put("icon",      text(cond, "icon"));
                }
            }

            List<Map<String, Object>> hourly = new ArrayList<>();
            NodeList days = doc.getElementsByTagName("forecastday");
            if (days.getLength() > 0) {
                Element day0 = (Element) days.item(0);
                NodeList hours = day0.getElementsByTagName("hour");
                for (int i = 0; i < hours.getLength(); i++) {
                    Element h = (Element) hours.item(i);
                    Map<String, Object> m = new HashMap<>();
                    m.put("time",       text(h, "time"));
                    m.put("tempC",      parseDouble(text(h, "temp_c")));
                    m.put("precipMM",   parseDouble(text(h, "precip_mm")));
                    m.put("precipProb", parseDouble(text(h, "chance_of_rain")));
                    NodeList hc = h.getElementsByTagName("condition");
                    if (hc.getLength() > 0) {
                        Element c = (Element) hc.item(0);
                        m.put("condition", text(c, "text"));
                        m.put("icon",      text(c, "icon"));
                    }
                    hourly.add(m);
                }
            }

            return new WeatherData(placeName, current, hourly);
        }
    }

    private static String text(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() == 0) return null;
        Node n = list.item(0).getFirstChild();
        return n == null ? null : n.getNodeValue();
    }
    private static Double parseDouble(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return null; }
    }
}

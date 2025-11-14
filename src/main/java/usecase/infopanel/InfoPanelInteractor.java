package usecase.infopanel;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoPanelInteractor implements InfoPanelInputBoundary {

    private final PointWeatherFetcher fetcher;
    private final InfoPanelOutputBoundary presenter;
    private final int zoomThreshold;

    public InfoPanelInteractor(PointWeatherFetcher fetcher,
                               InfoPanelOutputBoundary presenter,
                               int zoomThreshold) {
        this.fetcher = fetcher;
        this.presenter = presenter;
        this.zoomThreshold = zoomThreshold;
    }

    @Override
    public void execute(InfoPanelInputData req) {
        if (req.getZoom() < zoomThreshold) {
            presenter.presentError(InfoPanelError.ZOOM_TOO_LOW);
            return;
        }

        presenter.presentLoading();

        try {
            String xml = fetcher.fetch(req.getCenterLat(), req.getCenterLon());
            InfoPanelOutputData out = parseXmlToOutput(xml);
            presenter.present(out);
        } catch (Exception e) {
            presenter.presentError(InfoPanelError.FETCH_FAILED);
        }
    }

    private InfoPanelOutputData parseXmlToOutput(String xml) throws Exception {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        Document doc = f.newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        doc.getDocumentElement().normalize();

        // Place
        String place = "Selected location";
        NodeList locList = doc.getElementsByTagName("location");
        if (locList.getLength() > 0) {
            Element loc = (Element) locList.item(0);
            String name = text(loc, "name");
            if (name != null && !name.isBlank()) place = name;
        }

        // Current Temperature and weather
        Double curTemp = null;
        String condition = null;
        NodeList curList = doc.getElementsByTagName("current");
        if (curList.getLength() > 0) {
            Element cur = (Element) curList.item(0);
            curTemp = toDouble(text(cur, "temp_c"));
            NodeList condList = cur.getElementsByTagName("condition");
            if (condList.getLength() > 0) {
                Element c = (Element) condList.item(0);
                condition = text(c, "text");
            }
        }

        // Hourly Temperature List of the Day
        List<Double> hourlyTemps = new ArrayList<>();
        NodeList days = doc.getElementsByTagName("forecastday");
        if (days.getLength() > 0) {
            Element day0 = (Element) days.item(0);
            NodeList hours = day0.getElementsByTagName("hour");
            for (int i = 0; i < hours.getLength(); i++) {
                Element h = (Element) hours.item(i);
                hourlyTemps.add(toDouble(text(h, "temp_c")));
            }
        }

        // More Data
        Map<String, String> extra = new HashMap<>();
        if (curList.getLength() > 0) {
            Element cur = (Element) curList.item(0);
            putIfNotBlank(extra, "humidity",      text(cur, "humidity"));
            putIfNotBlank(extra, "wind_kph",      text(cur, "wind_kph"));
            putIfNotBlank(extra, "wind_dir",      text(cur, "wind_dir"));
            putIfNotBlank(extra, "pressure_mb",   text(cur, "pressure_mb"));
            putIfNotBlank(extra, "precip_mm",     text(cur, "precip_mm"));
            putIfNotBlank(extra, "feelslike_c",   text(cur, "feelslike_c"));
            putIfNotBlank(extra, "uv",            text(cur, "uv"));
            putIfNotBlank(extra, "cloud",         text(cur, "cloud"));
        }

        return new InfoPanelOutputData(
                place,
                curTemp,
                condition,
                hourlyTemps == null ? Collections.emptyList() : hourlyTemps,
                Instant.now(),
                extra
        );
    }

    private static String text(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() == 0) return null;
        Node n = list.item(0).getFirstChild();
        return n == null ? null : n.getNodeValue();
    }

    private static Double toDouble(String s) {
        try { return s == null ? null : Double.valueOf(s); }
        catch (Exception e) { return null; }
    }

    private static void putIfNotBlank(Map<String, String> m, String k, String v) {
        if (v != null && !v.isBlank()) m.put(k, v);
    }
}

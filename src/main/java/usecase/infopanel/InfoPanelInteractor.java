package usecase.infopanel;

import static interfaceadapter.infopanel.InfoPanelViewModel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class InfoPanelInteractor implements InfoPanelInputBoundary {

    private final PointWeatherFetcher fetcher;
    private final InfoPanelOutputBoundary presenter;

    public InfoPanelInteractor(PointWeatherFetcher fetcher,
                               InfoPanelOutputBoundary presenter) {
        this.fetcher = fetcher;
        this.presenter = presenter;
    }

    @Override
    public void execute(InfoPanelInputData req) {
        try {
            final String xml = fetcher.fetch(req.getCenterLat(), req.getCenterLon());
            final InfoPanelOutputData out = parseXmlToOutput(xml);
            presenter.present(out);
        }
        catch (IOException | ParserConfigurationException | SAXException exc) {
            presenter.presentError(InfoPanelError.FETCH_FAILED);
        }
    }

    private InfoPanelOutputData parseXmlToOutput(String xml)
            throws ParserConfigurationException, SAXException, IOException {

        final Document doc = parseDocument(xml);

        final String place = parsePlace(doc);
        final Double curTemp = parseCurrentTemp(doc);
        final String condition = parseCurrentCondition(doc);
        final List<Double> hourlyTemps = parseHourlyTemps(doc);
        final Map<String, String> extra = parseExtra(doc);

        return new InfoPanelOutputData(
                place,
                curTemp,
                condition,
                hourlyTemps,
                Instant.now(),
                extra
        );
    }

    // helpers
    private static String textOf(Element parent, String tag) {
        String result = null;

        final NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() > 0) {
            final Node node = list.item(0).getFirstChild();
            if (node != null) {
                result = node.getNodeValue();
            }
        }
        return result;
    }

    private static Double toDouble(String s) {
        Double result;

        if (s == null) {
            result = null;
        }
        else {
            try {
                result = Double.valueOf(s);
            }
            catch (NumberFormatException ignored) {
                result = null;
            }
        }

        return result;
    }

    private static void put(Map<String, String> strm, String strk, String strv) {
        if (strv != null && !strv.isBlank()) {
            strm.put(strk, strv);
        }
    }

    private Document parseDocument(String xml)
            throws ParserConfigurationException, SAXException, IOException {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.parse(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        document.getDocumentElement().normalize();
        return document;
    }

    private String parsePlace(Document doc) {
        String place = TAG_LOCATION;
        final NodeList locList = doc.getElementsByTagName(TAG_LOCATION);
        if (locList.getLength() > 0) {
            final Element loc = (Element) locList.item(0);
            final String name = textOf(loc, TAG_NAME);
            if (name != null && !name.isBlank()) {
                place = name;
            }
        }
        return place;
    }

    private Double parseCurrentTemp(Document doc) {
        Double curTemp = null;
        final NodeList curList = doc.getElementsByTagName(TAG_CURRENT);
        if (curList.getLength() > 0) {
            final Element cur = (Element) curList.item(0);
            curTemp = toDouble(textOf(cur, TAG_TEMP_C));
        }
        return curTemp;
    }

    private String parseCurrentCondition(Document doc) {
        String condition = null;
        final NodeList curList = doc.getElementsByTagName(TAG_CURRENT);
        if (curList.getLength() > 0) {
            final Element cur = (Element) curList.item(0);
            final NodeList condList = cur.getElementsByTagName(TAG_CONDITION);
            if (condList.getLength() > 0) {
                final Element c = (Element) condList.item(0);
                condition = textOf(c, TAG_CONDITION_TEXT);
            }
        }
        return condition;
    }

    private List<Double> parseHourlyTemps(Document doc) {
        final List<Double> hourlyTemps = new ArrayList<>();
        final NodeList days = doc.getElementsByTagName(TAG_FORECAST_DAY);
        if (days.getLength() > 0) {
            final Element day0 = (Element) days.item(0);
            final NodeList hours = day0.getElementsByTagName(TAG_HOUR);
            for (int i = 0; i < hours.getLength(); i++) {
                final Element h = (Element) hours.item(i);
                hourlyTemps.add(toDouble(textOf(h, TAG_TEMP_C)));
            }
        }
        return hourlyTemps;
    }

    private Map<String, String> parseExtra(Document doc) {
        final Map<String, String> extra = new HashMap<>();
        final NodeList curList = doc.getElementsByTagName(TAG_CURRENT);
        if (curList.getLength() > 0) {
            final Element cur = (Element) curList.item(0);
            put(extra, EXTRA_HUMIDITY, textOf(cur, EXTRA_HUMIDITY));
            put(extra, EXTRA_WIND_KPH, textOf(cur, EXTRA_WIND_KPH));
            put(extra, EXTRA_WIND_DIR, textOf(cur, EXTRA_WIND_DIR));
            put(extra, EXTRA_PRESSURE_MB, textOf(cur, EXTRA_PRESSURE_MB));
            put(extra, EXTRA_PRECIP_MM, textOf(cur, EXTRA_PRECIP_MM));
            put(extra, EXTRA_FEELSLIKE_C, textOf(cur, EXTRA_FEELSLIKE_C));
            put(extra, EXTRA_UV, textOf(cur, EXTRA_UV));
            put(extra, EXTRA_CLOUD, textOf(cur, EXTRA_CLOUD));
        }
        return extra;
    }
}

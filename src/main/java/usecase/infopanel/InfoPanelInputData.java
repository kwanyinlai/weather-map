package usecase.infopanel;

public class InfoPanelInputData {
    public final double centerLat, centerLon;
    public final int zoom;

    public InfoPanelInputData(double lat, double lon, int zoom) {
        this.centerLat = lat;
        this.centerLon = lon;
        this.zoom = zoom;
    }
    public double getCenterLat() { return centerLat; }
    public double getCenterLon() { return centerLon; }
    public int getZoom() { return zoom; }
}
package usecase.infopanel;

public class InfoPanelRequestModel {
    public final double centerLat, centerLon;
    public final int zoom;

    public InfoPanelRequestModel(double lat, double lon, int zoom) {
        this.centerLat = lat;
        this.centerLon = lon;
        this.zoom = zoom;
    }
    public double getCenterLat() { return centerLat; }
    public double getCenterLon() { return centerLon; }
    public int getZoom() { return zoom; }
}
package usecase.infopanel;

public class InfoPanelInputData {
    private final double centerLat;
    private final double centerLon;
    private final int zoom;

    public InfoPanelInputData(double centerLat, double centerLon, int zoom) {
        this.centerLat = centerLat;
        this.centerLon = centerLon;
        this.zoom = zoom;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public double getCenterLon() {
        return centerLon;
    }

    public int getZoom() {
        return zoom;
    }
}

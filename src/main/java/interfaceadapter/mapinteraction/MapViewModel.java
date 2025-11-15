package interfaceadapter.mapinteraction;

import entity.Location;
public class MapViewModel {
    private Location center;
    private int zoomLevel;
    private String error;

    public void update(Location newCenter, int newZoom, String error) {
        this.center = newCenter;
        this.zoomLevel = newZoom;
        this.error = error;
    }
    public Location getCenter() {
        return center;
    }
    public int getZoomLevel() {
        return zoomLevel;
    }
    public String getError() { return error; }
}
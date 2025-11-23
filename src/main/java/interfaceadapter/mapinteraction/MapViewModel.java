package interfaceadapter.mapinteraction;

import entity.Location;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
public class MapViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private Location center;
    private int zoomLevel;
    private String error;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void update(Location newCenter, int newZoom, String error) {
        Location oldCenter = this.center;
        int oldZoom = this.zoomLevel;
        String oldError = this.error;
        this.center = newCenter;
        this.zoomLevel = newZoom;
        this.error = error;
        support.firePropertyChange("center", oldCenter, newCenter);
        support.firePropertyChange("zoomLevel", oldZoom, newZoom);
        support.firePropertyChange("error", oldError, error);
    }
    public Location getCenter() {
        return center;
    }
    public int getZoomLevel() {
        return zoomLevel;
    }
    public String getError() { return error; }
}
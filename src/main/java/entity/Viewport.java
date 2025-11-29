package entity;
import org.openstreetmap.gui.jmapviewer.OsmMercator;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Viewport {
    private int pixelCenterX;
    private int pixelCenterY;
    private int viewWidth;
    private int viewHeight;
    private int zoomLevel;
    private static final OsmMercator mercator = OsmMercator.MERCATOR_256;
    private final int maxZoom;
    private final int minZoom;
    private final PropertyChangeSupport support;

    public Viewport(double initialPixelX, double initialPixelY, int initialWidth, int initialZoom,
                    int maxZoom, int minZoom, int initialHeight) {
        this.pixelCenterX = (int)initialPixelX;
        this.pixelCenterY = (int)initialPixelY;
        this.viewWidth = initialWidth;
        this.viewHeight = initialHeight;
        this.zoomLevel = initialZoom;
        this.maxZoom = maxZoom;
        this.minZoom = minZoom;
        this.support = new PropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);

    }

    public BoundingBox calculateBBox(){
        int currentZoom = this.zoomLevel;

        int topLeftPixelX = (pixelCenterX - (viewWidth / 2));
        int topLeftPixelY = (pixelCenterY - (viewHeight / 2));
        int bottomRightPixelX = (pixelCenterX + (viewWidth / 2));
        int bottomRightPixelY = (pixelCenterY + (viewHeight / 2));

        double topLeftLon = mercator.xToLon( topLeftPixelX, currentZoom);
        double topLeftLat = yConversion(topLeftPixelY,currentZoom,256);
        double bottomRightLon = mercator.xToLon( bottomRightPixelX, currentZoom);
        double bottomRightLat = yConversion(bottomRightPixelY,currentZoom,256);

        Location topLeft = new Location(topLeftLat, topLeftLon);
        Location bottomRight = new Location(bottomRightLat, bottomRightLon);
        return new BoundingBox(topLeft, bottomRight);
    }

    /**
     * A linearly interporlated function for converting the Y pixel coordinate on the map to its latitude value with a
     * range of (-inf, inf).
     *
     * @param pixelY The pixel position of a point on the map, based on the zoom level.
     * @param zoom The current zoom level
     * @param size The size of tiles on the map.
     * @return 90 when pixelY = 0, -90 when pixelY = 2^zoom * size, linearly interporlated.
     */
    public double yConversion(int pixelY, int zoom, int size){
        return ((pixelY / (Math.pow(2,zoom) * size)) * 180 - 90) * -1;
    }

    public Location getCentre() {
        double lon = mercator.xToLon(pixelCenterX, zoomLevel);
        double lat = mercator.yToLat(pixelCenterY, zoomLevel);
        return new Location(lat, lon);
    }
    public boolean isZoomValid(int newZoom) {
        return newZoom >= minZoom && newZoom <= maxZoom;
    }
    public void setZoomLevel(int zoomLevel) { this.zoomLevel = zoomLevel; }
    public int getMaxZoom() { return maxZoom; }
    public int getMinZoom() { return minZoom; }
    public void setPixelCenterX(int pixelCenterX) { this.pixelCenterX = pixelCenterX; }
    public void setPixelCenterY(int pixelCenterY) { this.pixelCenterY = pixelCenterY; }

    public int getZoomLevel() {
        return this.zoomLevel;
    }
    public double getPixelCenterX() {
        return pixelCenterX;
    }

    public double getPixelCenterY() {
        return pixelCenterY;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public PropertyChangeSupport getSupport(){
        return this.support;
    }

    public void setViewSize(Dimension data){
        viewWidth = data.width;
        viewHeight = data.height;
    }

    public int getBounedZoom(int offset){
        return Math.max(this.minZoom, Math.min(this.maxZoom, this.zoomLevel + offset));
    }
}

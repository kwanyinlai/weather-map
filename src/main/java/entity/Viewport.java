package entity;
import org.openstreetmap.gui.jmapviewer.OsmMercator;

import java.awt.*;

public class Viewport {
    private double pixelCenterX;
    private double pixelCenterY;
    private int viewWidth;
    private int viewHeight;
    private int zoomLevel;
    private static final OsmMercator mercator = OsmMercator.MERCATOR_256;
    private final int maxZoom;
    private final int minZoom;

    public Viewport(double initialPixelX, double initialPixelY, int initialWidth, int initialZoom,
                    int maxZoom, int minZoom, int initialHeight) {
        this.pixelCenterX = initialPixelX;
        this.pixelCenterY = initialPixelY;
        this.viewWidth = initialWidth;
        this.viewHeight = initialHeight;
        this.zoomLevel = initialZoom;
        this.maxZoom = maxZoom;
        this.minZoom = minZoom;
    }

    public BoundingBox calculateBBox(){
        int currentZoom = this.zoomLevel;

        long topLeftPixelX = (long) (pixelCenterX - (viewWidth / 2.0));
        long topLeftPixelY = (long) (pixelCenterY - (viewHeight / 2.0));
        long bottomRightPixelX = (long) (pixelCenterX + (viewWidth / 2.0));
        long bottomRightPixelY = (long) (pixelCenterY + (viewHeight / 2.0));

        double topLeftLon = mercator.xToLon((int) topLeftPixelX, currentZoom);
        double topLeftLat = mercator.yToLat((int) topLeftPixelY, currentZoom);
        double bottomRightLon = mercator.xToLon((int) bottomRightPixelX, currentZoom);
        double bottomRightLat = mercator.yToLat((int) bottomRightPixelY, currentZoom);

        Location topLeft = new Location(topLeftLat, topLeftLon);
        Location bottomRight = new Location(bottomRightLat, bottomRightLon);
        return new BoundingBox(topLeft, bottomRight);
    }

    public Location getCentre() {
        double lon = mercator.xToLon((int) pixelCenterX, zoomLevel);
        double lat = mercator.yToLat((int) pixelCenterY, zoomLevel);
        return new Location(lat, lon);
    }

    public float getZoomLevel() {
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

    public void setViewSize(Dimension data){
        viewWidth = data.width;
        viewHeight = data.height;
    }

    public int getBounedZoom(int offset){
        return Math.max(this.minZoom, Math.min(this.maxZoom, this.zoomLevel + offset));
    }
}

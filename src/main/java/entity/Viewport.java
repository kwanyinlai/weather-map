package entity;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Viewport {
    private final JMapViewer mapViewer;
    private Location centre;
    private static final OsmMercator mercator = OsmMercator.MERCATOR_256;
    private final int maxZoom;
    private final int minZoom;

    public Viewport(JMapViewer mapViewer, Location centre, int zoomLevel, int maxZoom, int minZoom) {
        this.mapViewer = mapViewer;
        this.centre = centre;
        this.maxZoom = maxZoom;
        this.minZoom= minZoom;
        mapViewer.setZoom( zoomLevel);
        Coordinate jmvCoordinate = new Coordinate(centre.getLatitude(), centre.getLongitude());
        mapViewer.setDisplayPosition(jmvCoordinate, zoomLevel);
    }

    public BoundingBox calculateBBox(){
        int viewWidth = mapViewer.getWidth();
        int viewHeight = mapViewer.getHeight();
        int currentZoom = this.mapViewer.getZoom();
        double centreLatitude = this.centre.getLatitude();
        double centreLongitude = this.centre.getLongitude();
        double centrePixelX = mercator.lonToX(centreLongitude, currentZoom);
        double centrePixelY = mercator.latToY(centreLatitude, currentZoom);
        long topLeftPixelX = (long) (centrePixelX - (viewWidth / 2.0));
        long topLeftPixelY = (long) (centrePixelY - (viewHeight / 2.0));
        long bottomRightPixelX = (long) (centrePixelX + (viewWidth / 2.0));
        long bottomRightPixelY = (long) (centrePixelY + (viewHeight / 2.0));
        double topLeftLon = mercator.xToLon((int) topLeftPixelX, currentZoom);
        double topLeftLat = mercator.yToLat((int) topLeftPixelY, currentZoom);
        double bottomRightLon = mercator.xToLon((int) bottomRightPixelX, currentZoom);
        double bottomRightLat = mercator.yToLat((int) bottomRightPixelY, currentZoom);
        Location topLeft = new Location(topLeftLat, topLeftLon);
        Location bottomRight = new Location(bottomRightLat, bottomRightLon);
        return new BoundingBox(topLeft, bottomRight);
    }

    public Location getCentre() {
        return centre;
    }

    public float getZoomLevel() {
        return mapViewer.getZoom();
    }

    public int getBounedZoom(){
        return Math.max(this.minZoom, Math.min(this.maxZoom, this.mapViewer.getZoom()));
    }
}

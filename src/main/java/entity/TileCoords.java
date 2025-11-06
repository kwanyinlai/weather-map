package entity;

import org.openstreetmap.gui.jmapviewer.OsmMercator;

public class TileCoords {
    public final int x;
    public final int y;
    public final int zoom;

    public TileCoords(int x, int y, int zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }

    public int getX(){return this.x;}

    public int getY(){return this.y;}

    public Location getLatLong() {
        OsmMercator merc = new OsmMercator(256);
        int px = x * 256 + 128;
        int py = y * 256 + 128;
        double lon = merc.xToLon(px, zoom);
        double lat = merc.yToLat(py, zoom);
        return new Location(lat, lon);
    }
}


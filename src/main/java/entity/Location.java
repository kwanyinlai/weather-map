package entity;

import org.openstreetmap.gui.jmapviewer.OsmMercator;

public class Location {
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public TileCoords getTileCoords(int zoom) {
        OsmMercator merc = new OsmMercator(256);
        double px = merc.lonToX(longitude, zoom);
        double py = merc.latToY(latitude,  zoom);
        int xtile = (int) Math.floor(px / 256.0);
        int ytile = (int) Math.floor(py / 256.0);
        return new TileCoords(xtile, ytile, zoom);
    }

    // convert both lat and lon to a value between 0-1, 0 being -180 or -90, 1 being 180 or 90.
    public double getNormalizedLatitude(){
        return (this.latitude + 90) / 180;
    }

    public double getNormalizedLongitude(){
        return (this.longitude + 180) / 360;
    }

    public Location getNormalizedLoation(){
        return new Location(getNormalizedLatitude(), getNormalizedLongitude());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location loc = (Location) obj;
            return latitude == loc.getLatitude() && longitude == loc.getLongitude();
        }
        return false;
    }
}

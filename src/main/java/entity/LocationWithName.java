package entity;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import java.util.Objects;

public class LocationWithName extends  Location {
        private final String name;


        public LocationWithName(String name, double latitude, double longitude) {
            super(latitude, longitude);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Coordinate getCoordinate() {
            return new Coordinate(getLatitude(), getLongitude());
        }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationWithName that = (LocationWithName) o;
        if (!Objects.equals(name, that.name)) return false;
        return Double.compare(getLatitude(), that.getLatitude()) == 0
                && Double.compare(getLongitude(), that.getLongitude()) == 0;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Double.hashCode(getLatitude());
        result = 31 * result + Double.hashCode(getLongitude());
        //the number 31 is an odd prime number , and it can reduce the hash confilct
        return result;
    }

        @Override
        public String toString() {
            return name + " (" + getLatitude() + ", " + getLongitude() + ")";
        }
    }


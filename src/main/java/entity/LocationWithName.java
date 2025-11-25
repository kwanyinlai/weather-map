package entity;

import org.openstreetmap.gui.jmapviewer.Coordinate;

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
        public String toString() {
            return name + " (" + getLatitude() + ", " + getLongitude() + ")";
        }
    }


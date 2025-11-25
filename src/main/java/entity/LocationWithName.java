package entity;

public class LocationWithName extends  Location {
        private final String name;


        public LocationWithName(String name, double latitude, double longitude) {
            super(latitude, longitude);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name + " (" + getLatitude() + ", " + getLongitude() + ")";
        }
    }


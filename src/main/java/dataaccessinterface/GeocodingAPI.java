package dataaccessinterface;

import entity.LocationWithName;

import java.util.List;


public interface GeocodingAPI {
    List<LocationWithName> geocode(String query);
}

package entity;

import java.time.Instant;

public class BookmarkedLocation extends Location {
    private final String name;
    private final Instant savedTime;

    // General constructor.
    public BookmarkedLocation(String name, double latitude, double longitude) {
        super(latitude, longitude);
        this.name = name;
        this.savedTime = Instant.now();
    }
    // Constructor For DAO specifically (used when converting the JSONObject to BookmarkedLocation).
    public BookmarkedLocation(String name, double latitude, double longitude, Instant savedTime) {
        super(latitude, longitude);
        this.name = name;
        this.savedTime = savedTime;
    }

    public String getName() {
        return name;
    }

    public Instant getSavedTime() {
        return savedTime;
    }

}

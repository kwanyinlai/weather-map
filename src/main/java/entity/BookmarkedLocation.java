package entity;

import java.time.Instant;

public class BookmarkedLocation extends Location {
    private final String name;
    private final Instant savedTime;

    public BookmarkedLocation(String name, double latitude, double longitude) {
        super(latitude, longitude);
        this.name = name;
        this.savedTime = Instant.now();
    }

    public String getName() {
        return name;
    }

    public Instant getSavedTime() {
        return savedTime;
    }

}

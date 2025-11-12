package entity;

import java.time.Instant;

public class BookmarkedLocation extends Location {
    private final String name;

    // General constructor.
    public BookmarkedLocation(String name, double latitude, double longitude) {
        super(latitude, longitude);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

package entity;

public class BookmarkedLocation extends Location {
    private final String name;

    // Constructor for the BookmarkedLocation.
    public BookmarkedLocation(String name, double latitude, double longitude) {
        super(latitude, longitude);
        this.name = name;
    }

    /**
     * Returns a {@link this.name} of this BookmarkedLocation.
     *
     * @return a {@link this.name}.
     */
    public String getName() {
        return name;
    }

}

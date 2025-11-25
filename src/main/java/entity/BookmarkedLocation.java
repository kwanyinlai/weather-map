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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkedLocation that = (BookmarkedLocation) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}

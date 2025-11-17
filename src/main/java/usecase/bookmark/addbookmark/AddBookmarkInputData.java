package usecase.bookmark.addbookmark;

/**
 * Data passed from the controller into the add–bookmark use case.
 * Contains only the information needed to create a bookmark.
 */
public final class AddBookmarkInputData {

    private final String name;
    private final double latitude;
    private final double longitude;

    /**
     * Constructs an input data object for adding a bookmark.
     *
     * @param name      human-readable name of the bookmark
     * @param latitude  latitude of the bookmarked location
     * @param longitude longitude of the bookmarked location
     */
    public AddBookmarkInputData(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

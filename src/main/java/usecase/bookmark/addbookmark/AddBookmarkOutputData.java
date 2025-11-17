package usecase.bookmark.addbookmark;

/**
 * Data returned by the add–bookmark use case to the presenter.
 */
public final class AddBookmarkOutputData {

    private final String name;
    private final double latitude;
    private final double longitude;

    /**
     * Constructs an output data object representing the added bookmark.
     *
     * @param name      name of the bookmark
     * @param latitude  latitude of the bookmarked location
     * @param longitude longitude of the bookmarked location
     */
    public AddBookmarkOutputData(String name, double latitude, double longitude) {
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

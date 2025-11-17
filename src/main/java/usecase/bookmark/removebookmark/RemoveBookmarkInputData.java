package usecase.bookmark.removebookmark;

/**
 * Data passed from the controller into the remove–bookmark use case.
 * Contains the information needed to identify which bookmark to delete.
 */
public final class RemoveBookmarkInputData {

    private final String name;
    private final double latitude;
    private final double longitude;

    /**
     * Constructs an input data object for removing a bookmark.
     *
     * @param name      name of the bookmark
     * @param latitude  latitude of the bookmarked location
     * @param longitude longitude of the bookmarked location
     */
    public RemoveBookmarkInputData(String name, double latitude, double longitude) {
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

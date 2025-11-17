package usecase.bookmark.removebookmark;

/**
 * Data returned by the remove–bookmark use case to the presenter.
 */
public final class RemoveBookmarkOutputData {

    private final String name;
    private final double latitude;
    private final double longitude;
    private final boolean removed;

    /**
     * Constructs an output data object representing the result
     * of a remove–bookmark operation.
     *
     * @param name      name of the bookmark
     * @param latitude  latitude of the bookmarked location
     * @param longitude longitude of the bookmarked location
     * @param removed   whether a matching bookmark was removed
     */
    public RemoveBookmarkOutputData(String name,
                                    double latitude,
                                    double longitude,
                                    boolean removed) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.removed = removed;
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

    public boolean isRemoved() {
        return removed;
    }
}

package usecase.bookmark.visitbookmark;

public final class VisitBookmarkInputData {
    private final double latitude;
    private final double longitude;

    public VisitBookmarkInputData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

package interfaceadapter.bookmark.visitbookmark;

import usecase.bookmark.visitbookmark.VisitBookmarkInputBoundary;
import usecase.bookmark.visitbookmark.VisitBookmarkInputData;

/**
 * Controller for the "visit bookmark" use case.
 */
public final class VisitBookmarkController {

    private final VisitBookmarkInputBoundary visitBookmarkInputBoundary;

    public VisitBookmarkController(VisitBookmarkInputBoundary visitBookmarkInputBoundary) {
        this.visitBookmarkInputBoundary = visitBookmarkInputBoundary;
    }

    /**
     * Requests that the viewport move to the given coordinates.
     */
    public void visitBookmark(double latitude, double longitude) {
        VisitBookmarkInputData inputData =
                new VisitBookmarkInputData(latitude, longitude);
        visitBookmarkInputBoundary.visitBookmark(inputData);
    }
}

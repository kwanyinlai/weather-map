package usecase.bookmark.visitbookmark;

public interface VisitBookmarkOutputBoundary {

    void present(VisitBookmarkOutputData outputData);

    void presentVisitBookmarkFailure(String errorMessage);
}

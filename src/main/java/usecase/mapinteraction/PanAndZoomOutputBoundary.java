package usecase.mapinteraction;

public interface PanAndZoomOutputBoundary {
    void presentSuccess(PanAndZoomOutputData outputData);
    void presentError(ZoomOutOfBoundsException e);
}
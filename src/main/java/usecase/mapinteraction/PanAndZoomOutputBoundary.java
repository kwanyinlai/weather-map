package usecase.mapinteraction;

public interface PanAndZoomOutputBoundary {
    void present(PanAndZoomOutputData outputData);
    void presentError(ZoomOutOfBoundsException e);
}
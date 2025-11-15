package usecase.mapinteraction;

public interface PanAndZoomInputBoundary {
    void handleZoom(PanAndZoomInputData input);
    void handlePan(PanAndZoomInputData input);
    void getBoundedZoom(PanAndZoomInputData input) throws ZoomOutOfBoundsException;
}

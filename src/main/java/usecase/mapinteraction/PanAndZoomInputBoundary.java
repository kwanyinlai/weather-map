package usecase.mapinteraction;

public interface PanAndZoomInputBoundary {
    PanAndZoomOutputData handleZoom(PanAndZoomInputData input);
    PanAndZoomOutputData handlePan(PanAndZoomInputData input);
}

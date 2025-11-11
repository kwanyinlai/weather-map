package usecase.mapinteraction;
import entity.Viewport;

public class PanAndZoomInputData {
    private final Integer zoomIncrement;
    private final Double dx;
    private final Double dy;
    private final Viewport currentViewport;
    public PanAndZoomInputData(Integer zoomIncrement, Viewport currentViewport) {
        this.zoomIncrement = zoomIncrement;
        this.currentViewport = currentViewport;
        this.dx = null;
        this.dy = null;
    }
    public PanAndZoomInputData(Double dx, Double dy, Viewport currentViewport) {
        this.dx = dx;
        this.dy = dy;
        this.currentViewport = currentViewport;
        this.zoomIncrement = null;
    }
    public Integer getZoomIncrement() { return zoomIncrement; }
    public Double getDx() { return dx; }
    public Double getDy() { return dy; }
    public Viewport getCurrentViewport() { return currentViewport; }
}

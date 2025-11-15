package usecase.mapinteraction;

public class PanAndZoomInputData {
    private final Integer zoomIncrement;
    private final Double dx;
    private final Double dy;
    // dx == null if the user doesn't pan, and dx == 0 if the user pans back to the same location
    public PanAndZoomInputData(Integer zoomIncrement) {
        this.zoomIncrement = zoomIncrement;
        this.dx = null;
        this.dy = null;
    }
    public PanAndZoomInputData(Double dx, Double dy) {
        this.dx = dx;
        this.dy = dy;
        this.zoomIncrement = null;
    }
    public Integer getZoomIncrement() { return zoomIncrement; }
    public Double getDx() { return dx; }
    public Double getDy() { return dy; }
}

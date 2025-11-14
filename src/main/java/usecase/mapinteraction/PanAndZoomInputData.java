package usecase.mapinteraction;

public class PanAndZoomInputData {
    private final Integer zoomIncrement;
    private final Double dx;
    private final Double dy;
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

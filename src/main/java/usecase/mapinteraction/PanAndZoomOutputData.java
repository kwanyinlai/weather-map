package usecase.mapinteraction;

import entity.Viewport;

public class PanAndZoomOutputData {
    private final Viewport updatedViewport;
    private final boolean isZoom;
    private final double dx;
    private final double dy;

    public PanAndZoomOutputData(Viewport newViewport, boolean isZoom, double dx, double dy) {
        this.updatedViewport = newViewport;
        this.isZoom = isZoom;
        this.dx = dx;
        this.dy = dy;
    }

    public boolean isZoom() { return isZoom; }
    public double getDx() { return dx; }
    public double getDy() { return dy; }

    public Viewport getUpdatedViewport() {
        return updatedViewport;
    }
}

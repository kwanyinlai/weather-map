package usecase.mapinteraction;

import entity.Viewport;

public class PanAndZoomOutputData {
    private final Viewport updatedViewport;
    private final boolean isSuccess;
    public PanAndZoomOutputData(Viewport newViewport, boolean isSuccess) {
        this.updatedViewport = newViewport;
        this.isSuccess = isSuccess;
    }
    public Viewport getUpdatedViewport() { return updatedViewport; }
    public boolean isSuccess() { return isSuccess; }
}

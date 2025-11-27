package usecase.mapnavigation;

import entity.Viewport;

public class PanAndZoomOutputData {
    private final Viewport viewport;
    private final boolean updated;

    public PanAndZoomOutputData(Viewport viewport, boolean updated) {
        this.viewport = viewport;
        this.updated = updated;
    }

    public Viewport getViewport() { return viewport; }
    public boolean isUpdated() { return updated; }
}

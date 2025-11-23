package usecase.mapinteraction;
import entity.Viewport;

import java.awt.*;

public class PanAndZoomUseCase implements PanAndZoomInputBoundary {
    private final Viewport viewport;
    public PanAndZoomUseCase(Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    public void updateViewport(PanAndZoomInputData input) {
        viewport.setZoomLevel(input.getZoomLevel());
        viewport.setPixelCenterX(input.getPixelCenterX());
        viewport.setPixelCenterY(input.getPixelCenterY());
        viewport.setViewSize(new Dimension(input.getViewWidth(), input.getViewHeight()));

        viewport.getSupport().firePropertyChange("viewportUpdated", null, viewport);

    }
}
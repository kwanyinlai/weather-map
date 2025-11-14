package usecase.mapinteraction;
import entity.Viewport;

public class PanAndZoomUseCase implements PanAndZoomInputBoundary {
    private final Viewport sharedViewport;
    public PanAndZoomUseCase(Viewport sharedViewport) {
        this.sharedViewport = sharedViewport;
    }
    @Override
    public PanAndZoomOutputData handleZoom(PanAndZoomInputData input) {
        int newZoom = sharedViewport.getZoomLevel() + input.getZoomIncrement();
        sharedViewport.setZoomLevel(newZoom);
        return new PanAndZoomOutputData(sharedViewport, true);
    }
    public PanAndZoomOutputData getBoundedZoom(PanAndZoomInputData input) throws ZoomOutOfBoundsException {
        int newZoom = sharedViewport.getZoomLevel() + input.getZoomIncrement();
        if (newZoom < sharedViewport.getMinZoom() || newZoom > sharedViewport.getMaxZoom()) {
            throw new ZoomOutOfBoundsException(
                    input.getZoomIncrement() > 0 ? "Reaches Maximum ZoomLevel" : "Reaches Minimum ZoomLevel"
            );
        }
        sharedViewport.setZoomLevel(newZoom);
        return new PanAndZoomOutputData(sharedViewport, true);
    }
    @Override
    public PanAndZoomOutputData handlePan(PanAndZoomInputData input) {
        double newPixelX = sharedViewport.getPixelCenterX() + input.getDx();
        double newPixelY = sharedViewport.getPixelCenterY() + input.getDy();
        sharedViewport.setPixelCenterX(newPixelX);
        sharedViewport.setPixelCenterY(newPixelY);
        return new PanAndZoomOutputData(sharedViewport, true);
    }



}
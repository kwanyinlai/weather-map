package usecase.mapinteraction;
import entity.Viewport;

public class PanAndZoomUseCase implements PanAndZoomInputBoundary {
    private final Viewport sharedViewport;
    private final PanAndZoomOutputBoundary outputBoundary;
    public PanAndZoomUseCase(Viewport sharedViewport, PanAndZoomOutputBoundary outputBoundary) {
        this.sharedViewport = sharedViewport;
        this.outputBoundary = outputBoundary;
    }
    @Override
    public void handleZoom(PanAndZoomInputData input) {
        int newZoom = sharedViewport.getZoomLevel() + input.getZoomIncrement();
        sharedViewport.setZoomLevel(newZoom);
        outputBoundary.present(new PanAndZoomOutputData(sharedViewport, true,0,0));
    }
    @Override
    public void getBoundedZoom(PanAndZoomInputData input) throws ZoomOutOfBoundsException {
        int newZoom = sharedViewport.getZoomLevel() + input.getZoomIncrement();
        if (newZoom < sharedViewport.getMinZoom() || newZoom > sharedViewport.getMaxZoom()) {
            throw new ZoomOutOfBoundsException(
                    input.getZoomIncrement() > 0 ? "Reaches Maximum ZoomLevel" : "Reaches Minimum ZoomLevel"
            );
        }
        sharedViewport.setZoomLevel(newZoom);
        outputBoundary.present(new PanAndZoomOutputData(sharedViewport, true,0,0));
    }
    @Override
    public void handlePan(PanAndZoomInputData input) {
        double newPixelX = sharedViewport.getPixelCenterX() - input.getDx();
        double newPixelY = sharedViewport.getPixelCenterY() - input.getDy();

        sharedViewport.setPixelCenterX(newPixelX);
        sharedViewport.setPixelCenterY(newPixelY);

        outputBoundary.present(
                new PanAndZoomOutputData(sharedViewport, false, input.getDx(), input.getDy())
        );
    }



}
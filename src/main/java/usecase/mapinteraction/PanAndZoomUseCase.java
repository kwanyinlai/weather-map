package usecase.mapinteraction;
import entity.BoundingBox;
import entity.Location;
import entity.Viewport;

public class PanAndZoomUseCase implements PanAndZoomInputBoundary {
    private final BoundingBox globalBBox = new BoundingBox(
            new Location(90, -180),
            new Location(-90, 180)
    );

    @Override
    public PanAndZoomOutputData handleZoom(PanAndZoomInputData input) {
        Viewport currentViewport = input.getCurrentViewport();
        int newZoom = currentViewport.getZoomLevel() + input.getZoomIncrement();
        if (!currentViewport.isZoomValid(newZoom)) {
            String feedback;
            if (input.getZoomIncrement() > 0) {
                feedback = "Reach maximum zoom level";
            } else {
                feedback = "Reach minimum zoom level";
            }
            return new PanAndZoomOutputData(currentViewport, feedback, false);
        }
        currentViewport.setZoomLevel(newZoom);
        return new PanAndZoomOutputData(currentViewport, "Zoom is successful", true);
    }

    @Override
    public PanAndZoomOutputData handlePan(PanAndZoomInputData input) {
        Viewport currentViewport = input.getCurrentViewport();
        double dx = input.getDx();
        double dy = input.getDy();
        double newPixelX = currentViewport.getPixelCenterX() + dx;
        double newPixelY = currentViewport.getPixelCenterY() + dy;
        Viewport tempViewport = new Viewport(
                newPixelX, newPixelY,
                currentViewport.getViewWidth(),
                currentViewport.getViewHeight(),
                currentViewport.getZoomLevel(),
                currentViewport.getMaxZoom(),
                currentViewport.getMinZoom()
        );
        BoundingBox newViewBBox = tempViewport.calculateBBox();
        Location topLeft = newViewBBox.getTopLeft();
        Location bottomRight = newViewBBox.getBottomRight();
        double centerLat = topLeft.getLatitude() - (topLeft.getLatitude() - bottomRight.getLatitude()) / 2;
        double centerLon = topLeft.getLongitude() + (bottomRight.getLongitude() - topLeft.getLongitude()) / 2;
        Location newCenter = new Location(centerLat, centerLon);
        boolean isWithinGlobal = globalBBox.locationInBBox(newCenter);
        if (!isWithinGlobal) {
            return new PanAndZoomOutputData(currentViewport, "Reaches the boundary of map", false);
        }
        currentViewport.setPixelCenterX(newPixelX);
        currentViewport.setPixelCenterY(newPixelY);
        return new PanAndZoomOutputData(currentViewport, "Pan is successful", true);
    }

}
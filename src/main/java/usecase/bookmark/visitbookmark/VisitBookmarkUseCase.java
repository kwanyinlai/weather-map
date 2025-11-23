package usecase.bookmark.visitbookmark;

import entity.Viewport;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import usecase.mapinteraction.PanAndZoomOutputBoundary;
import usecase.mapinteraction.PanAndZoomOutputData;
import usecase.weatherLayers.update.UpdateOverlayUseCase;

/**
 * Use case: move the map viewport to a bookmarked location.
 */
public final class VisitBookmarkUseCase implements VisitBookmarkInputBoundary {

    private static final OsmMercator MERCATOR = OsmMercator.MERCATOR_256;

    private final Viewport viewport;
    private final UpdateOverlayUseCase updateOverlayUseCase;
    private final PanAndZoomOutputBoundary panAndZoomOutputBoundary;
    private final VisitBookmarkOutputBoundary errorOutputBoundary;

    public VisitBookmarkUseCase(Viewport viewport,
                                UpdateOverlayUseCase updateOverlayUseCase,
                                PanAndZoomOutputBoundary panAndZoomOutputBoundary,
                                VisitBookmarkOutputBoundary errorOutputBoundary) {
        this.viewport = viewport;
        this.updateOverlayUseCase = updateOverlayUseCase;
        this.panAndZoomOutputBoundary = panAndZoomOutputBoundary;
        this.errorOutputBoundary = errorOutputBoundary;
    }

    @Override
    public void visitBookmark(VisitBookmarkInputData inputData) {
        double latitude = inputData.getLatitude();
        double longitude = inputData.getLongitude();

        // Basic validation.
        if (Double.isNaN(latitude) || Double.isNaN(longitude)
                || latitude < -90 || latitude > 90
                || longitude < -180 || longitude > 180) {
            errorOutputBoundary.presentVisitBookmarkFailure("Invalid bookmark coordinates.");
            return;
        }

        int zoom = viewport.getZoomLevel();
        double pixelX = MERCATOR.lonToX(longitude, zoom);
        double pixelY = MERCATOR.latToY(latitude, zoom);

        viewport.setPixelCenterX((int) pixelX);
        viewport.setPixelCenterY((int) pixelY);

        // Update the overlay.
        if (updateOverlayUseCase != null) {
            updateOverlayUseCase.update();
        }

        // Present the result using PanAndZoomPresenter.
        PanAndZoomOutputData outputData =
                new PanAndZoomOutputData(viewport, true);
        panAndZoomOutputBoundary.present(outputData);
    }
}

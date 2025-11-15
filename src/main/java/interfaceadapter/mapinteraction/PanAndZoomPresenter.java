package interfaceadapter.mapinteraction;

import entity.Viewport;
import usecase.mapinteraction.PanAndZoomOutputBoundary;
import usecase.mapinteraction.PanAndZoomOutputData;
import usecase.mapinteraction.ZoomOutOfBoundsException;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import java.awt.Point;

public class PanAndZoomPresenter implements PanAndZoomOutputBoundary {
    private final JMapViewer mapViewer;
    private final MapViewModel viewModel;
    public PanAndZoomPresenter(JMapViewer mapViewer, MapViewModel viewModel) {
        this.mapViewer = mapViewer;
        this.viewModel = viewModel;
    }
    @Override
    public void present(PanAndZoomOutputData outputData) {
        Viewport updatedViewport = outputData.getUpdatedViewport();
        double pixelX = updatedViewport.getPixelCenterX();
        double pixelY = updatedViewport.getPixelCenterY();
        Point newCenter = new Point((int) pixelX, (int) pixelY);
        mapViewer.setCenter(newCenter);
        int newZoom = updatedViewport.getZoomLevel();
        mapViewer.setZoom(newZoom);
    }
    @Override
    public void presentError(ZoomOutOfBoundsException e) {
        viewModel.update(null, 0, e.getMessage());
    }
}

package interfaceadapter.mapinteraction;

import usecase.mapinteraction.PanAndZoomOutputBoundary;
import usecase.mapinteraction.PanAndZoomOutputData;
import usecase.mapinteraction.ZoomOutOfBoundsException;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

public class PanAndZoomPresenter implements PanAndZoomOutputBoundary {
    private final JMapViewer mapViewer;
    private final MapViewModel viewModel;
    public PanAndZoomPresenter(JMapViewer mapViewer, MapViewModel viewModel) {
        this.mapViewer = mapViewer;
        this.viewModel = viewModel;
    }
    @Override
    public void present(PanAndZoomOutputData outputData) {
        if (!outputData.isZoom()) {
            mapViewer.moveMap((int) outputData.getDx(), (int) outputData.getDy());
            return;
        }

        mapViewer.setZoom(outputData.getUpdatedViewport().getZoomLevel());
    }
    @Override
    public void presentError(ZoomOutOfBoundsException e) {
        viewModel.update(null, 0, e.getMessage());
    }
}

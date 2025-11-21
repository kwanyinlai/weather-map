package interfaceadapter.mapinteraction;

import entity.Viewport;
import usecase.mapinteraction.PanAndZoomOutputBoundary;
import usecase.mapinteraction.PanAndZoomOutputData;
import usecase.mapinteraction.ZoomOutOfBoundsException;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

public class PanAndZoomPresenter implements PanAndZoomOutputBoundary {

    private final Viewport viewport;
    private final JMapViewer mapViewer;
    private final MapViewModel mapViewModel;

    public PanAndZoomPresenter(Viewport viewport,
                               JMapViewer mapViewer,
                               MapViewModel mapViewModel) {
        this.viewport = viewport;
        this.mapViewer = mapViewer;
        this.mapViewModel = mapViewModel;
    }


    @Override
    public void present(PanAndZoomOutputData outputData) {
    }
}


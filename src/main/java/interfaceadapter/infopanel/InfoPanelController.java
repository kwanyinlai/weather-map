package interfaceadapter.infopanel;

import usecase.infopanel.InfoPanelInputBoundary;
import usecase.infopanel.InfoPanelInputData;

public class InfoPanelController {
    private final InfoPanelInputBoundary interactor;

    public InfoPanelController(InfoPanelInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void onViewportStable(double centerLat, double centerLon, int zoom) {
        interactor.execute(new InfoPanelInputData(centerLat, centerLon, zoom));
    }
}

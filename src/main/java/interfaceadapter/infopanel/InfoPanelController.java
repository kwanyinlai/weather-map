package interfaceadapter.infopanel;

import entity.Location;
import entity.TileCoords;
import usecase.infopanel.*;

import javax.swing.Timer;

public final class InfoPanelController {
    private final InfoPanelInputBoundary interactor;
    private final InfoPanelOutputBoundary presenter;
    private final int popUpZoomThreshold;

    private final Timer debounce = new Timer(200, e -> fireIfTargetChanged());
    private double pendingLat, pendingLon;
    private int pendingZoom;

    private TileCoords lastRequestedTile;

    public InfoPanelController(InfoPanelInputBoundary interactor,
                               InfoPanelOutputBoundary presenter,
                               int popUpZoomThreshold) {
        this.interactor = interactor;
        this.presenter = presenter;
        this.popUpZoomThreshold = popUpZoomThreshold;
        this.debounce.setRepeats(false);
    }

    public void onViewportChanged(double centerLat, double centerLon, int zoom) {
        if (zoom < popUpZoomThreshold) {
            presenter.presentError(InfoPanelError.HIDDEN_BY_ZOOM);
            return;
        }
        pendingLat  = centerLat;
        pendingLon  = centerLon;
        pendingZoom = zoom;

        presenter.presentLoading();
        debounce.restart();
    }

    public void onCloseRequested() {
        presenter.presentError(InfoPanelError.USER_CLOSED);
    }

    private void fireIfTargetChanged() {
        TileCoords current = new Location(pendingLat, pendingLon).getTileCoords(pendingZoom);

        lastRequestedTile = current;
        interactor.execute(new InfoPanelInputData(pendingLat, pendingLon, pendingZoom));
    }

    private static boolean sameTile(TileCoords a, TileCoords b) {
        return a != null && b != null &&
                a.x == b.x && a.y == b.y && a.zoom == b.zoom;
    }
}

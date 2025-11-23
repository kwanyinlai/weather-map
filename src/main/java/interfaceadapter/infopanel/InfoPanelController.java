package interfaceadapter.infopanel;

import entity.Location;
import entity.TileCoords;
import usecase.infopanel.InfoPanelError;
import usecase.infopanel.InfoPanelInputBoundary;
import usecase.infopanel.InfoPanelInputData;
import usecase.infopanel.InfoPanelOutputBoundary;

import javax.swing.Timer;

public final class InfoPanelController {
    private final InfoPanelInputBoundary interactor;
    private final InfoPanelOutputBoundary presenter;
    private final int popUpZoomThreshold;

    private final Timer debounce = new Timer(300, e -> fireIfTargetChanged());
    private double pendingLat, pendingLon; private int pendingZoom;
    private TileCoords lastRequestedTile;
    private boolean userClosed;

    public InfoPanelController(InfoPanelInputBoundary interactor,
                               InfoPanelOutputBoundary presenter,
                               int popUpZoomThreshold) {
        this.interactor = interactor;
        this.presenter = presenter;
        this.popUpZoomThreshold = popUpZoomThreshold;
        this.debounce.setRepeats(false);
    }

    public void onViewportChanged(double centerLat, double centerLon, int zoom) {
        if (zoom > popUpZoomThreshold) {
            lastRequestedTile = null;
            userClosed = false;
            presenter.presentError(InfoPanelError.HIDDEN_BY_ZOOM);
            return;
        }

        if (userClosed) {
            presenter.presentError(InfoPanelError.USER_CLOSED);
            return;
        }

        presenter.presentLoading();
        pendingLat = centerLat; pendingLon = centerLon; pendingZoom = zoom;
        debounce.restart();
    }

    public void onCloseRequested() {
        userClosed = true;
        presenter.presentError(InfoPanelError.USER_CLOSED);
    }

    private void fireIfTargetChanged() {
        TileCoords current = new Location(pendingLat, pendingLon).getTileCoords(pendingZoom);
        if (lastRequestedTile != null &&
                lastRequestedTile.x == current.x &&
                lastRequestedTile.y == current.y &&
                lastRequestedTile.zoom == current.zoom) {
            return;
        }
        lastRequestedTile = current;
        interactor.execute(new InfoPanelInputData(pendingLat, pendingLon, pendingZoom));
    }
}

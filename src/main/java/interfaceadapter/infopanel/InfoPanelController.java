package interfaceadapter.infopanel;

import entity.Location;
import entity.TileCoords;
import usecase.infopanel.*;

import javax.swing.Timer;

public final class InfoPanelController {
    private final InfoPanelInputBoundary interactor;
    private final InfoPanelOutputBoundary presenter;

    private final Timer debounce = new Timer(300, e -> fireIfTargetChanged());
    private double pendingLat, pendingLon;
    private int pendingZoom;

    private TileCoords lastRequestedTile;
    private TileCoords lastDismissedTile;

    public InfoPanelController(InfoPanelInputBoundary interactor,
                               InfoPanelOutputBoundary presenter) {
        this.interactor = interactor;
        this.presenter = presenter;
        this.debounce.setRepeats(false);
    }

    public void onViewportChanged(double lat, double lon, int zoom) {
        var current = new Location(lat, lon).getTileCoords(zoom);
        if (current != null && current.equals(lastDismissedTile)) {
            presenter.presentError(InfoPanelError.USER_CLOSED);
            return;
        }
        pendingLat = lat;
        pendingLon = lon;
        pendingZoom = zoom;
        presenter.presentLoading();
        debounce.restart();
    }

    public void onCloseRequested() {
        if (lastRequestedTile != null) lastDismissedTile = lastRequestedTile;
        presenter.presentError(InfoPanelError.USER_CLOSED);
    }

    private void fireIfTargetChanged() {
        var current = new Location(pendingLat, pendingLon).getTileCoords(pendingZoom);
        if (current != null && current.equals(lastRequestedTile)) return;
        lastRequestedTile = current;
        interactor.execute(new InfoPanelInputData(pendingLat, pendingLon, pendingZoom));
    }
}

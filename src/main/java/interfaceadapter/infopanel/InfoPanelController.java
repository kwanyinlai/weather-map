package interfaceadapter.infopanel;

import javax.swing.Timer;

import entity.Location;
import entity.TileCoords;
import usecase.infopanel.InfoPanelError;
import usecase.infopanel.InfoPanelInputBoundary;
import usecase.infopanel.InfoPanelInputData;
import usecase.infopanel.InfoPanelOutputBoundary;

public final class InfoPanelController {
    private final InfoPanelInputBoundary interactor;
    private final InfoPanelOutputBoundary presenter;

    private final Timer debounce = new Timer(300, event -> fireIfTargetChanged());
    private double pendingLat;
    private double pendingLon;
    private int pendingZoom;

    private TileCoords lastRequestedTile;
    private TileCoords lastDismissedTile;

    public InfoPanelController(InfoPanelInputBoundary interactor,
                               InfoPanelOutputBoundary presenter) {
        this.interactor = interactor;
        this.presenter = presenter;
        this.debounce.setRepeats(false);
    }

    /**
     * Called whenever the map viewport changes (panning or zooming).
     * This method:
     * <ul>
     *     <li>Converts the given latitude/longitude and zoom level into a tile coordinate.</li>
     *     <li>If the newly computed tile is the same as the last tile for which the user
     *         explicitly closed the info panel, it reports a {@code USER_CLOSED} error and
     *         does nothing else.</li>
     *     <li>Otherwise, it stores the new viewport as a pending request, shows a loading
     *         state via the presenter, and restarts the debounce timer so the actual
     *         fetch/update runs after a short delay.</li>
     * </ul>
     *
     * @param lat  latitude of the center of the current viewport, in degrees
     * @param lon  longitude of the center of the current viewport, in degrees
     * @param zoom zoom level of the current viewport
     */
    public void onViewportChanged(double lat, double lon, int zoom) {
        final var current = new Location(lat, lon).getTileCoords(zoom);
        if (current != null && current.equals(lastDismissedTile)) {
            presenter.presentError(InfoPanelError.USER_CLOSED);
        }
        pendingLat = lat;
        pendingLon = lon;
        pendingZoom = zoom;
        presenter.presentLoading();
        debounce.restart();
    }

    /**
     * Handles the user's request to close the info panel.
     * Remembers the last requested tile as the last dismissed tile (if any)
     * and notifies the presenter that the panel was closed by the user.
     */
    public void onCloseRequested() {
        if (lastRequestedTile != null) {
            lastDismissedTile = lastRequestedTile;
        }
        presenter.presentError(InfoPanelError.USER_CLOSED);
    }

    private void fireIfTargetChanged() {
        final var current = new Location(pendingLat, pendingLon).getTileCoords(pendingZoom);
        if (current != null && current.equals(lastRequestedTile)) {
            presenter.presentError(InfoPanelError.USER_CLOSED);
        }
        lastRequestedTile = current;
        interactor.execute(new InfoPanelInputData(pendingLat, pendingLon, pendingZoom));
    }
}

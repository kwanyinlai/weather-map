package interfaceadapter.mapsettings.loadmapsettings;

import entity.LayerNotFoundException;
import entity.OverlayManager;
import entity.Viewport;
import entity.WeatherType;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import usecase.mapsettings.loadmapsettings.LoadMapSettingsOutputBoundary;
import usecase.mapsettings.loadmapsettings.LoadMapSettingsOutputData;
import usecase.weatherlayers.layers.ChangeLayerInputBoundary;
import usecase.weatherlayers.layers.ChangeLayerInputData;

/**
 * Presenter for automatically loading map settings on startup.
 * Applies settings directly to the viewport and overlay manager.
 */
public final class AutoLoadMapSettingsPresenter implements LoadMapSettingsOutputBoundary {

    private final Viewport viewport;
    private final ChangeLayerInputBoundary changeLayerUseCase;
    private static final OsmMercator MERCATOR = OsmMercator.MERCATOR_256;

    /**
     * Creates a presenter that applies settings directly to the viewport and overlay manager.
     *
     * @param viewport the viewport to update
     * @param overlayManager the overlay manager to update
     * @param changeLayerUseCase use case for changing the weather layer
     */
    public AutoLoadMapSettingsPresenter(Viewport viewport,
                                        ChangeLayerInputBoundary changeLayerUseCase) {
        this.viewport = viewport;
        this.changeLayerUseCase = changeLayerUseCase;
    }

    @Override
    public void presentLoadedSettings(LoadMapSettingsOutputData outputData) {
        double latitude = outputData.getCenterLatitude();
        double longitude = outputData.getCenterLongitude();
        int zoomLevel = outputData.getZoomLevel();
        WeatherType weatherType = outputData.getWeatherType();

        // Update viewport zoom level
        viewport.setZoomLevel(zoomLevel);

        // Convert lat/lon to pixel coordinates and update viewport center
        double pixelX = MERCATOR.lonToX(longitude, zoomLevel);
        double pixelY = MERCATOR.latToY(latitude, zoomLevel);
        viewport.setPixelCenterX((int) pixelX);
        viewport.setPixelCenterY((int) pixelY);

        // Update weather layer if a saved type exists
        if (weatherType != null) {
            try {
                changeLayerUseCase.change(new ChangeLayerInputData(weatherType));
            } catch (LayerNotFoundException e) {
                // If the layer doesn't exist, just ignore it
            }
        }

        // Fire property change to notify listeners
        viewport.getSupport().firePropertyChange("viewportUpdated", null, viewport);
    }

    @Override
    public void presentNoSavedSettings() {
        // Use default settings
    }

    @Override
    public void presentLoadSettingsFailure(String errorMessage) {
        // Use default settings
    }
}


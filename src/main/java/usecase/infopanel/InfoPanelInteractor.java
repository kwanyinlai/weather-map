package usecase.infopanel;

import dataaccessinterface.WeatherGateway;
import entity.WeatherData;
import java.time.Instant;

public class InfoPanelInteractor implements InfoPanelInputBoundary {
    private final WeatherGateway weatherGateway;
    private final InfoPanelOutputBoundary presenter;
    private final int zoomThreshold;

    public InfoPanelInteractor(WeatherGateway weatherGateway,
                               InfoPanelOutputBoundary presenter,
                               int zoomThreshold) {
        this.weatherGateway = weatherGateway;
        this.presenter = presenter;
        this.zoomThreshold = zoomThreshold;
    }

    @Override
    public void execute(InfoPanelInputData req) {
        if (req.getZoom() < zoomThreshold) {
            presenter.presentError(InfoPanelError.ZOOM_TOO_LOW);
            return;
        }
        presenter.presentLoading();
        try {
            WeatherData wd = weatherGateway.fetchCurrentAndHourly(req.getCenterLat(), req.getCenterLon());
            presenter.present(InfoPanelOutputData.fromWeatherData(wd, Instant.now()));
        } catch (Exception e) {
            presenter.presentError(InfoPanelError.FETCH_FAILED);
        }
    }
}

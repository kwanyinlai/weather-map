package usecase.infopanel;

import dataaccessinterface.WeatherGateway;

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
    public void execute(InfoPanelRequestModel req) {
        if (req.getZoom() < zoomThreshold) {
            presenter.presentError("Zoom in to view local weather.");
            return;
        }

        presenter.presentLoading();

        try {
            WeatherGateway.Result r =
                    weatherGateway.fetchCurrentAndHourly(req.getCenterLat(), req.getCenterLon());
            long fetchedAtEpoch = System.currentTimeMillis() / 1000;
            InfoPanelResponseModel out = new InfoPanelResponseModel(
                    r.place,
                    r.curTemp,
                    r.condition,
                    r.fTemps,
                    fetchedAtEpoch);

            presenter.present(out);

        } catch (Exception e) {
            presenter.presentError("Unable to load weather. Retry.");
        }
    }
}

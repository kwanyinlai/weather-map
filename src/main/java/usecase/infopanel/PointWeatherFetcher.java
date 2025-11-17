package usecase.infopanel;

public interface PointWeatherFetcher {
    String fetch(double lat, double lon) throws Exception;
}

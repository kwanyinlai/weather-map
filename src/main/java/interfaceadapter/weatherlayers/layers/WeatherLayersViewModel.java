package interfaceadapter.weatherlayers.layers;

public class WeatherLayersViewModel {
    private double currentOpacity;
    public WeatherLayersViewModel(double op){
        this.currentOpacity = op;
    }
    public double getCurrentOpacity(){return currentOpacity;}

    public void setCurrentOpacity(double currentOpacity) {
        this.currentOpacity = currentOpacity;
    }
}

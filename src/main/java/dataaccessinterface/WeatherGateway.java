package dataaccessinterface;

import java.util.List;

public interface WeatherGateway {
    Result fetchCurrentAndHourly(double lat, double lon);

    class Result {
        public final String place;   // name of the place
        public final double curTemp;   // current temperature
        public final String condition;   // the weather
        public final List<Double> fTemps;   //futrue temperatures

        public Result(String place, double curTemp, String condition, List<Double> fTemps) {
            this.place = place;
            this.curTemp = curTemp;
            this.condition = condition;
            this.fTemps = fTemps;
        }
    }
}

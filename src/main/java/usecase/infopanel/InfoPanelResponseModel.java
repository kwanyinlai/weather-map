package usecase.infopanel;
import java.util.List;

public class InfoPanelResponseModel {
    public final String place;   // name of the place
    public double curTemp;   // current temperature
    public final String condition;   // the weather
    public List<Double> fTemps;   //futrue temperatures
    public final long fetchedAtEpoch;

    public InfoPanelResponseModel(String place, double curTemp, String condition,
                                      List<Double> fTemps, long fetchedAtEpoch) {
        this.place = place;
        this.curTemp = curTemp;
        this.condition = condition;
        this.fTemps = fTemps;
        this.fetchedAtEpoch = fetchedAtEpoch;
    }
}
package interfaceadapter.infopanel;

import usecase.infopanel.InfoPanelError;
import java.time.Instant;
import java.util.List;

public class InfoPanelViewModel {
    public boolean loading;
    public InfoPanelError error;
    public String placeName;
    public Double tempC;
    public String condition;
    public List<Double> hourlyTemps;
    public Instant fetchedAt;
}

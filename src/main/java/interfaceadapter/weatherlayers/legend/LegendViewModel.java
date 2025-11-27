package interfaceadapter.weatherlayers.legend;

import interfaceadapter.ViewModel;

public class LegendViewModel extends ViewModel<LegendState> {
    public LegendViewModel(){
        super("Legend");
        setState(new LegendState());
    }
}

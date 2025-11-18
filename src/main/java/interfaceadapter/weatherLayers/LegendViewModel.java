package interfaceadapter.weatherLayers;

import interfaceadapter.ViewModel;

public class LegendViewModel extends ViewModel<LegendState> {
    public LegendViewModel(){
        super("Legend");
        setState(new LegendState());
    }
}

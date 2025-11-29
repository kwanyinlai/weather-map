package interfaceadapter.weatherlayers.legend;

import usecase.weatherlayers.layers.ChangeLegendOutputData;
import usecase.weatherlayers.layers.UpdateLegendOutputBoundary;

public class LegendPresenter implements UpdateLegendOutputBoundary {
    private final LegendViewModel legendViewModel;

    public LegendPresenter(LegendViewModel vm){
        legendViewModel = vm;
    }

    @Override
    public void setLegend(ChangeLegendOutputData data){
        LegendState state = legendViewModel.getState();
        state.setImage(data.getLegendImg());
        legendViewModel.firePropertyChange("legend");
    }
}

package interfaceadapter.weatherLayers;

import usecase.weatherLayers.layers.ChangeLegendOutputData;
import usecase.weatherLayers.layers.UpdateLegendOutputBoundary;

public class LegendPresenter implements UpdateLegendOutputBoundary {
    private final LegendViewModel legendViewModel;

    public LegendPresenter(LegendViewModel vm){
        legendViewModel = vm;
    }

    @Override
    public void setLegend(ChangeLegendOutputData data){
        System.out.println("Presenter Updated");
        LegendState state = legendViewModel.getState();
        state.setImage(data.getLegendImg());
        legendViewModel.firePropertyChange("legend");
    }
}

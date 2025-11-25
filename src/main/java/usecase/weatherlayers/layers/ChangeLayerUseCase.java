package usecase.weatherlayers.layers;

import dataaccessinterface.GradientLegendLoader;
import dataaccessobjects.GradientLoader;
import entity.LayerNotFoundException;
import entity.OverlayManager;
import entity.WeatherType;

public class ChangeLayerUseCase implements ChangeLayerInputBoundary{
    private final OverlayManager overlayManager;
    private final ChangeLayerOutputBoundary layersPresenter;
    private final UpdateLegendOutputBoundary legendPresenter;
    private final GradientLegendLoader legendStorage;

    public ChangeLayerUseCase(OverlayManager OM, ChangeLayerOutputBoundary layerPresenter,
                              UpdateLegendOutputBoundary legendPresenter){
        this.overlayManager = OM;
        this.layersPresenter = layerPresenter;
        this.legendPresenter = legendPresenter;
        this.legendStorage = new GradientLoader();
        //Initialize gradient legend
        try{
            this.change(new ChangeLayerInputData(WeatherType.values()[0]));
        } catch (LayerNotFoundException ignored) {
        }

    }

    @Override
    public void change(ChangeLayerInputData data) throws LayerNotFoundException{
            this.overlayManager.setSelected(data.getType());
            layersPresenter.updateOpacity(new ChangeLayersOutputData(overlayManager.getSelectedOpacity()));
            legendPresenter.setLegend(new ChangeLegendOutputData(legendStorage.getLegend(data.getType())));
    }
}


package usecase.weatherLayers.layers;

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
        try{
            this.change(new ChangeLayerInputData(WeatherType.Tmp2m));
        } catch (LayerNotFoundException ignored) {
        }

    }

    @Override
    public void change(ChangeLayerInputData data) throws LayerNotFoundException{
            this.overlayManager.setSelected(data.getType());
            layersPresenter.updateOpacity(new ChangeLayersOutputData(overlayManager.getSelectedOpacity()));
            legendPresenter.setLegend(new ChangeLegendOutputData(legendStorage.getLegend(data.getType())));
        System.out.println("Usecase Updated");
    }
}


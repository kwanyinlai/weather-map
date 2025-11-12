package interfaceadapter.weatherLayers;

import usecase.weatherLayers.update.UpdateOverlaySizeInputBoundary;
import usecase.weatherLayers.update.UpdateOverlaySizeInputData;

import java.awt.*;

public class UpdateOverlaySizeController {
    private final UpdateOverlaySizeInputBoundary input;

    public UpdateOverlaySizeController(UpdateOverlaySizeInputBoundary in){
        this.input = in;
    }

    public void changeSize(Dimension size){
        input.changeSize(new UpdateOverlaySizeInputData(size));
    }
}

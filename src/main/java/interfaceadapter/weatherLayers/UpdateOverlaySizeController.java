package interfaceadapter.weatherLayers;

import usecase.weatherLayers.update.UpdateOverlayInputBoundary;
import usecase.weatherLayers.update.UpdateOverlaySizeInputBoundary;
import usecase.weatherLayers.update.UpdateOverlaySizeInputData;

import java.awt.*;

public class UpdateOverlaySizeController {
    private final UpdateOverlaySizeInputBoundary input;
    private final UpdateOverlayInputBoundary updateInput;

    public UpdateOverlaySizeController(UpdateOverlaySizeInputBoundary sizeInput, UpdateOverlayInputBoundary updateInput){
        this.updateInput = updateInput;
        this.input = sizeInput;
    }

    public void changeSize(Dimension size){
        input.changeSize(new UpdateOverlaySizeInputData(size));
        updateInput.update();
    }
}

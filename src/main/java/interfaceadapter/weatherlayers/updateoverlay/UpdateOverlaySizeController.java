package interfaceadapter.weatherlayers.updateoverlay;

import usecase.weatherlayers.updateoverlay.UpdateOverlayInputBoundary;
import usecase.weatherlayers.updateoverlay.UpdateOverlaySizeInputBoundary;
import usecase.weatherlayers.updateoverlay.UpdateOverlaySizeInputData;

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

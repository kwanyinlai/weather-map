package interfaceadapter.weatherLayers;

import usecase.weatherLayers.update.UpdateOverlayInputBoundary;

public class UpdateOverlayController {
    private final UpdateOverlayInputBoundary input;

    public UpdateOverlayController(UpdateOverlayInputBoundary in){
        this.input = in;
    }

    public void update(){
        input.update();
    }

}

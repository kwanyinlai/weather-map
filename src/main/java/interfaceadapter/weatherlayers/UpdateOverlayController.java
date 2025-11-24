package interfaceadapter.weatherlayers;

import usecase.weatherlayers.update.UpdateOverlayInputBoundary;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UpdateOverlayController implements PropertyChangeListener {
    private final UpdateOverlayInputBoundary input;

    public UpdateOverlayController(UpdateOverlayInputBoundary in){
        this.input = in;
    }

    public void update(){
        input.update();
    }

    @Override
    public void propertyChange(PropertyChangeEvent e){
        update();
    }

}

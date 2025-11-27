package usecase.weatherlayers.updateoverlay;

import java.awt.*;

public class UpdateOverlaySizeInputData {
    private final Dimension size;

    public UpdateOverlaySizeInputData(Dimension size){
        this.size = size;
    }

    public Dimension getSize(){
        return size;
    }
}

package usecase.weatherlayers.update;

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

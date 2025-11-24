package usecase.weatherlayers.update;

import java.awt.image.BufferedImage;

public class UpdateOverlayOutputData {
    private BufferedImage image;
    public UpdateOverlayOutputData(BufferedImage img){
        this.image = img;
    }

    public BufferedImage getImage(){
        return this.image;
    }
}

package interfaceadapter.weatherLayers;

import java.awt.image.BufferedImage;

public class UpdateOverlayViewModel {
    private BufferedImage image;
    public UpdateOverlayViewModel(BufferedImage img){
        image = img;
    }

    public void updateImage(BufferedImage img){
        image = img;
    }

    public BufferedImage getImage(){
        return image;
    }
}

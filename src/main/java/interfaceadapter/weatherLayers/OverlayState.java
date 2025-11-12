package interfaceadapter.weatherLayers;

import java.awt.image.BufferedImage;

public class OverlayState {
    private BufferedImage image;

    public void setImage(BufferedImage img){
        image = img;
    }

    public BufferedImage getImage(){
        return image;
    }
}

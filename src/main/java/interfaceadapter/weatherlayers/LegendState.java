package interfaceadapter.weatherlayers;

import java.awt.image.BufferedImage;

public class LegendState {
    private BufferedImage image;

    public void setImage(BufferedImage img){
        image = img;
    }

    public BufferedImage getImage(){
        return image;
    }
}

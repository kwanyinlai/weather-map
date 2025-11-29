package usecase.weatherlayers.layers;

import java.awt.image.BufferedImage;

public class ChangeLegendOutputData {
    private final BufferedImage legendImg;

    public ChangeLegendOutputData(BufferedImage img){legendImg = img;}

    public BufferedImage getLegendImg(){return legendImg;}
}

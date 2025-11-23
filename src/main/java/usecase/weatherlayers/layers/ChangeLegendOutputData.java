package usecase.weatherlayers.layers;

import java.awt.image.BufferedImage;

public class ChangeLegendOutputData {
    private final BufferedImage legend_img;

    public ChangeLegendOutputData(BufferedImage img){legend_img = img;}

    public BufferedImage getLegendImg(){return legend_img;}
}

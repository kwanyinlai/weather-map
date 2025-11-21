package dataaccessinterface;

import entity.WeatherType;

import java.awt.image.BufferedImage;

public interface GradientLegendLoader {
    /**
     * Return a BufferedImage corrosponding to the specified weather type. Return a blank BufferedImage
     * if no legend image was found corrsponding to the specified type.
     * @param type the weather type of the legend
     * @return A BufferedImage of a gradient legend for the specified type.
     */
    BufferedImage getLegend(WeatherType type);

}

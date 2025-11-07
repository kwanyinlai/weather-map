package dataaccessinterface;

import entity.WeatherTile;

import java.awt.image.BufferedImage;
import java.util.List;

/** Interface which interacts with the weather API
 *
 */
public interface WeatherTileApiFetcher {

    /** Make an API call to <a href="https://weathermaps.weatherapi.com">WeatherMaps</a> API to request
     * image data, and return the image data.
     *
     * @param tile the tile for which image data is to be collected for
     * @return image data associated with <code>tile</code>
     * @throws TileNotFoundException If image data for <code>tile</code> could not be parsed, or if the tile given is invalid
     */
    BufferedImage getWeatherTileImageData(WeatherTile tile) throws TileNotFoundException;



    /** Make an API call to <a href="https://weathermaps.weatherapi.com">WeatherMaps</a> API to request
     * image data, and return a list of the image data for all the {@link WeatherTile} in the list,
     * in the same order in which they are given in the parameter list
     *
     * @param tiles the list of tile for which image data is to be collected for
     * @return the list of image data associated with <code>tile</code> given in the same order as given
     * in <code>tile</code>. Return a null if the image data cannot be found.
     */
    List<BufferedImage> getListOfWeatherTileImageData(List<WeatherTile> tiles);
}

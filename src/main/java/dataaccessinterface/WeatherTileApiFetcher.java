package dataaccessinterface;

import entity.WeatherTile;

import java.awt.image.BufferedImage;

public interface WeatherTileApiFetcher {

    /** Make an API call to <a href="https://weathermaps.weatherapi.com">WeatherMaps</a> API to request
     * image data, and return the image data.
     *
     * @param tile the tile for which image data is to be collected for
     * @return image data associated with <code>tile</code>
     * @throws TileNotFoundException If image data for <code>tile</code> could not be parsed, or if the tile given is invalid
     */
    public BufferedImage getWeatherTileImageData(WeatherTile tile) throws TileNotFoundException;
}

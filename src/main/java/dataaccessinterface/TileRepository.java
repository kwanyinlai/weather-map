package dataaccessinterface;
import entity.Tile;
import entity.WeatherTile;
import entity.WeatherType;

import java.awt.image.BufferedImage;


/** Interface which returns the image data to given tiles
 *
 */
public interface TileRepository {

    /** Return the image data associated with the given tile parameters.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @param zoom the zoom-level of the tile
     * @param timestamp the time which the time corresponds to
     * @param weatherType the weather type corresponding the {@link WeatherType}
     * @return the BufferedImage of the image data of the tile
     * @throws TileNotFoundException raised if the parameters do not correspond to a valid tile
     */
    BufferedImage getTileImageData(int x, int y, int zoom, java.time.Instant timestamp, WeatherType weatherType) throws TileNotFoundException;

    /** Return the image data associated with the given tile.
     *
     * @param tile the tile which the image data should be retrieved from
     * @return the BufferedImage of the image data of the tile
     * @throws TileNotFoundException raised if the tile does not correspond to valid image data
     */
    BufferedImage getTileImageData(WeatherTile tile) throws TileNotFoundException;

    /** Takes a tile parameter (w/o image data) and retrieves the data associated with
     * it and adds it to cache
     *
     * @param tile the tile to be added to cache
     * @throws TileNotFoundException raised if the tile does not correspond to valid image data
     */
    void addTileToCache(WeatherTile tile) throws TileNotFoundException;
}

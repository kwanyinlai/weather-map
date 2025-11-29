package dataaccessinterface;
import dataaccessobjects.tilejobs.TileCompletedListener;
import entity.*;

import java.awt.image.BufferedImage;
import java.time.Instant;


/** Interface which returns the image data to given tiles.
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
     * @throws TileNotFoundException raised if the parameters do not
     * correspond to a valid tile
     */
    BufferedImage getTileImageData(int x, int y, int zoom,
                                   java.time.Instant timestamp,
                                   WeatherType weatherType)
            throws TileNotFoundException;

    /** Return the image data associated with the given tile.
     *
     * @param tile the tile which the image data should be retrieved from
     * @return the BufferedImage of the image data of the tile
     * @throws TileNotFoundException raised if the tile does not correspond
     * to valid image data
     */
    BufferedImage getTileImageData(WeatherTile tile)
            throws TileNotFoundException;

    /**
     * Takes a tile parameter (w/o image data) and retrieves the data
     * associated with it and adds it to cache. Once the tile retrieved,
     * additionally notify all listeners.
     * @param tile the tile to retrieve
     * @param topLeft the top left Vector of the Viewport
     * @param botRight the bot right Vector
     * @param tileCoords the coordinates of the centre of the viewport
     * @param currentTime the current program time
     */
    void requestTile(WeatherTile tile, Vector topLeft, Vector botRight,
                     Location tileCoords, Instant currentTime);

    /** Add a {@link TileCompletedListener} which will be notified whenever
     * a requested tileis retrieved from cache.
     * @param listener the listener to be added.
     */
    void addListener(TileCompletedListener listener);

    /** Return if the tile is currently already stored in cache.
     *
     * @param tile the tiel to check against.
     * @return true if the tile exists in cache, false otherwise.
     */
    boolean inCache(WeatherTile tile);
}

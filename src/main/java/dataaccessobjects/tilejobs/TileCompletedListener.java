package dataaccessobjects.tilejobs;

import dataaccessinterface.TileNotFoundException;
import entity.IncompleteTile;

import java.awt.image.BufferedImage;

public interface TileCompletedListener {
    /** Pass the incomplete tile and the correspodning image data
     * to all listeners once a tile fetch is complete.
     * @param tile the meta data of the tile.
     * @param tileImage the image of the tile.
     */
    void onTileCompleted(IncompleteTile tile, BufferedImage tileImage);

    /** Pass the incomplete tile and an exception representing
     * what went wrong with the tile fetch back to all listeners.
     * @param tile the meta data of the tile
     * @param e the exception representing what went wrong
     */
    void onTileFailed(IncompleteTile tile, TileNotFoundException e);
}

package dataaccessobjects.tilejobs;

import dataaccessinterface.TileNotFoundException;
import entity.IncompleteTile;

import java.awt.image.BufferedImage;

public interface TileCompletedListener {
    void onTileCompleted(IncompleteTile tile, BufferedImage tileImage);
    void onTileFailed(IncompleteTile tile, TileNotFoundException e);
}

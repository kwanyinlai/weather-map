package dataaccessinterface;
import entity.Tile;
import entity.WeatherTile;

import java.awt.image.BufferedImage;

public interface TileRepository {
    BufferedImage getTileImageData(int x, int y, double zoom, java.time.Instant timestamp) throws TileNotFoundException;
    BufferedImage getTileImageData(WeatherTile tile) throws TileNotFoundException;
    void addTileToCache(WeatherTile tile);
}

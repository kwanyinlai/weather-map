package dataaccessinterface;
import entity.Tile;
import entity.WeatherTile;

import java.awt.image.BufferedImage;

public interface TileRepository {
    public BufferedImage getTileImageData(int x, int y, int zoom, java.time.Instant timestamp);
    public BufferedImage getTileImageData(WeatherTile tile);
    public void addTileToCache(WeatherTile tile);
}

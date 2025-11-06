package dataaccessobjects;

import dataaccessinterface.TileNotFoundException;
import dataaccessinterface.TileRepository;
import dataaccessinterface.WeatherTileApiFetcher;
import entity.WeatherTile;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;


/** Concrete implementation of a {@link TileRepository} interface, used to
 *  store image data for {@link WeatherTile} efficiently using a cache. Cache
 *  grows up to a fixed size, and stores nodes by Least Recently Used.
 */
public class CachedTileRepository implements TileRepository {
    private final WeatherTileApiFetcher weatherTileApiFetcher = new OkHttpsWeatherTileApiFetcher();
    private TileCacheLinkedHash<String, BufferedImage> tileCache;


    private static class TileCacheLinkedHash<String, BufferedImage> extends LinkedHashMap<String, BufferedImage>{
        private final int maxSize;
        public TileCacheLinkedHash(int maxSize) {
            this.maxSize = maxSize;
        }

        protected boolean removeEldestEntry(){
            return size() > maxSize;
        }
    }
    /**
     * @param tileCacheSize The maximum cache size for the cache
     */
    public CachedTileRepository(int tileCacheSize){
        tileHash = new TileCacheLinkedHash<>(tileCacheSize);
    }

    /** Return the tile image data associated with the given parms
     *
     * @param x The x-coordinate of the tile
     * @param y The y-coordinate of the tile
     * @param zoom The zoom level of the tile
     * @param timestamp The timestamp of the tile, up to 3 hours from the current time
     * @return The image data of the tile associated with the params or <code> null </code>
     *         if the params refer to an invalid tile
     */
    public BufferedImage getTileImageData(int x, int y, double zoom, java.time.Instant timestamp) throws TileNotFoundException {
        try {
            return getTileImageDataFromStringKey(x+","+y+","+zoom+","+timestamp);
        }
        catch (TileNotFoundException e) {
            throw new TileNotFoundException(e.getMessage());
        }
    }

    /** Return the tile image data associated with the Tile object
     *
     * @param tile The tile to which the image data is to be retrieved for
     * @return The image data of the tile associated with the params or <code> null </code>
     *         if the params refer to an invalid tile
     */
    public BufferedImage getTileImageData(WeatherTile tile) throws TileNotFoundException {
        String tileKey = tile.generateKey();
        try {
            return getTileImageDataFromStringKey(tileKey);
        }
        catch (TileNotFoundException e) {
            throw new TileNotFoundException(e.getMessage());
        }
    }

    private BufferedImage getTileImageDataFromStringKey(String tileKey) throws TileNotFoundException {
        if (tileCache.containsKey(tileKey)){
            return tileCache.get(tileKey).imageData;
        }
        else {
            try {
                return getTileImageFromAPI(tile);
            } catch (TileNotFoundException e) {
                throw new TileNotFoundException(e.getMessage());
            }
        }
    }

    private BufferedImage getTileImageFromAPI(WeatherTile tile) throws TileNotFoundException {
        try {
            return weatherTileApiFetcher.getWeatherTileImageData(tile);
        } catch (TileNotFoundException e) {
            throw new TileNotFoundException(e.getMessage());
        }

    }


    /**
     *
     * @param tile the tile to be added to cache
     */
    public void addTileToCache(WeatherTile tile) throws TileNotFoundException {
        if (tileCache.containsKey(tile.generateKey())) {
            return;
        }
        BufferedImage imageData;
        try {
            imageData = weatherTileApiFetcher.getWeatherTileImageData(tile);
        } catch (TileNotFoundException e) {
            throw new TileNotFoundException(e.getMessage());
        }
        tileCache.put(tile.generateKey(), imageData);
    }

    public void forceClearOutdatedCache(java.time.Instant time){
    }

}
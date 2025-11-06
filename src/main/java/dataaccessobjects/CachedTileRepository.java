package dataaccessobjects;

import dataaccessinterface.TileNotFoundException;
import dataaccessinterface.TileRepository;
import dataaccessinterface.WeatherTileApiFetcher;
import entity.TileCoords;
import entity.WeatherTile;
import entity.WeatherType;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/** Concrete implementation of a {@link TileRepository} interface, used to
 *  store image data for {@link WeatherTile} efficiently using a cache. Cache
 *  grows up to a fixed size, and stores nodes by Least Recently Used.
 */
public class CachedTileRepository implements TileRepository {
    private final WeatherTileApiFetcher weatherTileApiFetcher = new OkHttpsWeatherTileApiFetcher();
    private Map<WeatherTile, BufferedImage> tileCache;

    /**
     * @param tileCacheSize The maximum cache size for the cache
     */
    public CachedTileRepository(int tileCacheSize){
        tileCache = Collections.synchronizedMap(
                new LinkedHashMap<WeatherTile, BufferedImage>(){
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<WeatherTile, BufferedImage> eldest) {
                        return size() > tileCacheSize;
                    }
                }
        );
    }

    /** Return the tile image data associated with the given parms
     *
     * @param x The x-coordinate of the tile
     * @param y The y-coordinate of the tile
     * @param zoom The zoom level of the tile
     * @param timestamp The timestamp of the tile, up to 3 hours from the current time
     * @param weatherType The weather type of the tile from the {@link WeatherType} enum
     * @return The image data of the tile associated with the params or <code> null </code>
     *         if the params refer to an invalid tile
     */
    public BufferedImage getTileImageData(
            int x, int y, int zoom, java.time.Instant timestamp, WeatherType weatherType)
            throws TileNotFoundException {
        try {
            return getTileImageData(
                    new WeatherTile(
                            new TileCoords(x,y,zoom),
                            timestamp,
                            weatherType
                    )
            );
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
        try {
            return getTileImageDataHelper(tile);
        }
        catch (TileNotFoundException e) {
            throw new TileNotFoundException(e.getMessage());
        }
    }

    private BufferedImage getTileImageDataHelper(WeatherTile tile) throws TileNotFoundException {
        if (tileCache.containsKey(tile)){
            return tileCache.get(tile);
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
        tileCache.put(tile, imageData);
    }

    /** Remove all <code><WeatherTile, BufferedImage></code> key-pairs
     * that have a timestamp after the current time.
     */
    public void forceClearOutdatedCache(){
        for(WeatherTile tile: tileCache.keySet()){
            if(tile.getTimestamp().equals(java.time.Instant.now())){
                tileCache.remove(tile);
            }
        }
    }

    /** Remove all <code><WeatherTile, BufferedImage></code> key-pairs
     * that have a timestamp after the given time.
     *
     * @param time all tiles that have timestamp before this time will be removed
     */
    public void forceClearOutdatedCache(java.time.Instant time){
        for(WeatherTile tile: tileCache.keySet()){
            if(tile.getTimestamp().equals(time)){
                tileCache.remove(tile);
            }
        }
    }

}
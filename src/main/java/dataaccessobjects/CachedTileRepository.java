package dataaccessobjects;

import dataaccessinterface.TileNotFoundException;
import dataaccessinterface.TileRepository;
import dataaccessinterface.WeatherTileApiFetcher;
import dataaccessobjects.tilejobs.TileCompletedListener;
import dataaccessobjects.tilejobs.TileJob;
import dataaccessobjects.tilejobs.TileJobSystem;
import entity.*;
import entity.Vector;

import java.awt.image.BufferedImage;
import java.util.*;


/** Concrete implementation of a {@link TileRepository} interface, used to
 *  store image data for {@link WeatherTile} efficiently using a cache. Cache
 *  grows up to a fixed size, and stores nodes by Least Recently Used.
 */
public class CachedTileRepository implements TileRepository {
    private final WeatherTileApiFetcher weatherTileApiFetcher = new OkHttpsWeatherTileApiFetcher();
    private Map<WeatherTile, BufferedImage> tileCache;
    private final TileJobSystem tileJobSystem;
    private final List<TileCompletedListener> listeners = new ArrayList<>();

    private static CachedTileRepository instance;

    public static CachedTileRepository getInstance(){
        return instance;
    }


    public void requestTile(WeatherTile tile, Vector topLeft, Vector botRight, Location viewportState){
        if (tileCache.containsKey(tile)){
            BufferedImage image = tileCache.get(tile);
            for (TileCompletedListener listener : listeners){
                listener.onTileCompleted(new IncompleteTile(topLeft, botRight, viewportState, tile), image);
            }
        }
        else{
            TileJob tileJob = new TileJob(tile, topLeft, botRight, viewportState);
            tileJobSystem.submitJob(tileJob);
            tileJob.getFuture().component2().thenAccept(future -> {
                for (TileCompletedListener listener : listeners){
                    listener.onTileCompleted(tileJob.getFuture().component1(), future);
                }
            }).exceptionally(e -> {
                for (TileCompletedListener listener : listeners){
                    listener.onTileFailed(tileJob.getFuture().component1(), (TileNotFoundException) e);
                }
                return null;
            });
        }
    }

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
        tileJobSystem = new TileJobSystem(4);
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
                BufferedImage image = getTileImageFromAPI(tile);
                addTileImagePairToCache(tile, image);
                return image;
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

    private void addTileImagePairToCache(WeatherTile tile, BufferedImage image) {
        if (tileCache.containsKey(tile)){
            return;
        }
        else{
            tileCache.put(tile, image);
        }
    }

    public void addTileToCache(WeatherTile tile) throws TileNotFoundException {
        if (tileCache.containsKey(tile)) {
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
        tileCache.entrySet().removeIf(
                tileImagePair->
                        tileImagePair.
                                getKey().
                                getTimestamp().
                                isAfter(java.time.Instant.now()
                                )
        );
    }

    /** Remove all <code><WeatherTile, BufferedImage></code> key-pairs
     * that have a timestamp after the given time.
     *
     * @param time all tiles that have timestamp before this time will be removed
     */
    public void forceClearOutdatedCache(java.time.Instant time){
        tileCache.entrySet().removeIf(
                tileImagePair->
                        tileImagePair.
                                getKey().
                                getTimestamp().
                                isAfter(time)
        );
    }

    public void clearCache(){
        tileCache.clear();
    }

    public void addListener(TileCompletedListener listener){
        listeners.add(listener);
    }
}
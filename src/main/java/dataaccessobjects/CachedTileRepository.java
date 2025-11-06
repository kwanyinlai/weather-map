package dataaccessobjects;

import dataaccessinterface.TileNotFoundException;
import dataaccessinterface.TileRepository;
import dataaccessinterface.WeatherTileApiFetcher;
import entity.WeatherTile;
import java.awt.image.BufferedImage;
import java.util.HashMap;


/** Concrete implementation of a {@link TileRepository} interface, used to
 *  store image data for {@link WeatherTile} efficiently using a cache. Cache
 *  grows up to a fixed size, and stores nodes by Least Recently Used.
 */
public class CachedTileRepository implements TileRepository {
    private final WeatherTileApiFetcher weatherTileApiFetcher = new OkHttpsWeatherTileApiFetcher();
    private HashMap<String, CacheEntry> tileHash;
    private CacheEntryList cacheEntryList; // linked list used for efficient removal and adding.
    private final int tileCacheSize; // max size for the given cache

    private static class CacheEntry {
        private BufferedImage imageData;
        private CachedTileRepository.CacheEntry next;
        private CachedTileRepository.CacheEntry prev;

        /**
         * @param imageData The image data of a specific tile
         */
        protected CacheEntry(BufferedImage imageData) {
            this.imageData = imageData;
        }
    }

    /** storing head and tail node for doubly linked list
     *  might only need head node though
     */
    private static class CacheEntryList {
        private CacheEntry head;
        private CacheEntry tail;


        public void addCacheEntry(CacheEntry entry) {
            if (this.head == null) {
                this.head = entry;
                this.tail = entry;
                this.tail.next = this.head;
            }
            else{
                this.tail.next = entry;
                entry.next = this.head;
                this.tail = entry;
            }
        }

    }

    /**
     * @param tileCacheSize The maximum cache size for the cache
     */
    public CachedTileRepository(int tileCacheSize){
        tileHash = new HashMap<>();
        tiles = new List<>();
        this.tileCacheSize = tileCacheSize;
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
        if (tileHash.containsKey(tileKey)){
            return tileHash.get(tileKey).imageData;
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
     * @param tile
     */
    public void addTileToCache(WeatherTile tile){
        if (tileHash.containsKey(tile.generateKey())){
            return;
        }
        BufferedImage imageData = getTileImageDataFromAPI(tile);
        tileHash.put(tile.getKey(), imageData);
        tileCacheSize.add(tile);
    }

    public void clearOutdatedCache(java.time.Instant time){
    }

}
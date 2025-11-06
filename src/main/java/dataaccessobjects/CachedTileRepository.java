package dataaccessobjects;

import dataaccessinterface.TileRepository;
import entity.WeatherTile;
import java.awt.image.BufferedImage;
import java.util.HashMap;


/** Concrete implementation of a {@link TileRepository} interface, used to
 *  store image data for {@link WeatherTile} efficiently using a cache. Cache
 *  grows up to a fixed size, and stores nodes by Least Recently Used.
 */
public class CachedTileRepository implements TileRepository {

    private HashMap<String, CacheEntry> tileHash;
    private CacheEntryList cacheEntryList; // linked list used for efficient removal and adding.
    private int tileCacheSize; // max size for the given cache

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

    private static class CacheEntryList {
        private CacheEntry head;
        private CacheEntry tail;

        public CacheEntryList(CacheEntry firstNode) {
            return;
        }

        public void addCacheEntry(CacheEntry entry) {
            if (this.head == null) {
                this.head = entry;
                this.tail = entry;
                this.tail.next = this.head;
            }
            else{
                this.tail.next = entry;
                entry.next = this.tail;
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
    public BufferedImage getTileImageData(int x, int y, double zoom, java.time.Instant timestamp){
        return getTileImageDataFromStringKey(x+","+y+","+zoom+","+timestamp);
    }

    /** Return the tile image data associated with the Tile object
     *
     * @param tile The tile to which the image data is to be retrieved for
     * @return The image data of the tile associated with the params or <code> null </code>
     *         if the params refer to an invalid tile
     */
    public BufferedImage getTileImageData(WeatherTile tile){
        String tileKey = tile.generateKey();
        return getTileImageDataFromStringKey(tileKey);
    }

    private BufferedImage getTileImageDataFromStringKey(String tileKey){
        if (tileHash.containsKey(tileKey)){
            return tileHash.get(tileKey).imageData;
        }
        else {
            return getTileImageFromAPI(tile);
        }
    }

    private BufferedImage getTileImageFromAPI(WeatherTile tile){
        return null;
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
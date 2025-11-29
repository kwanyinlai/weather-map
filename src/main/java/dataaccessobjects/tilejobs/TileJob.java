package dataaccessobjects.tilejobs;

import entity.IncompleteTile;
import entity.WeatherTile;
import entity.Vector;
import entity.Location;
import kotlin.Pair;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/** Class which represents a tile retrieveal job.
 */
public class TileJob {
    private final IncompleteTile tileData;
    private final Pair<IncompleteTile, CompletableFuture<BufferedImage>> futureData;

    public TileJob(WeatherTile tile, Vector topLeft, Vector botRight,
                   Location viewportState, Instant time) {
        this.tileData = new IncompleteTile(topLeft, botRight, viewportState, tile, time);
        CompletableFuture<BufferedImage> future = new CompletableFuture<>();
        this.futureData = new Pair<>(this.tileData, future);
    }

    /** Return only the {@link WeatherTile} object associated with this tile job.
     *
     * @return the {@link }WeatherTile} (note: not the {@link IncompleteTile})
     */
    public WeatherTile getTile() {
        return tileData.getWeatherTile();
    }

    /** Return a {@link Pair} of the meta data of the tile and its
     * {@link CompletableFuture}.
     * @return component1 stores the tile meta data and component2 stores the image.
     */
    public Pair<IncompleteTile, CompletableFuture<BufferedImage>> getFuture(){
        return futureData;
    }

}

package dataaccessobjects.tilejobs;

import entity.*;
import kotlin.Pair;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;


public class TileJob {

    private final CompletableFuture<BufferedImage> future = new CompletableFuture<>();
    private final IncompleteTile tileData;
    private final Pair<IncompleteTile, CompletableFuture<BufferedImage>> futureData;


    public TileJob(WeatherTile tile, Vector topLeft, Vector botRight, Location viewportState, Instant time) {
        this.tileData = new IncompleteTile(topLeft, botRight, viewportState, tile, time);
        this.futureData = new Pair<>(this.tileData, this.future);
    }

    public WeatherTile getTile() {
        return tileData.getWeatherTile();
    }

    public Pair<IncompleteTile, CompletableFuture<BufferedImage>> getFuture(){
        return futureData;
    }

}
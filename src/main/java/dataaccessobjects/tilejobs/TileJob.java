package dataaccessobjects.tilejobs;

import entity.*;
import kotlin.Pair;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;


public class TileJob {
    private final WeatherTile tile;
    private final Vector topLeft;
    private final Vector botRight;
    private final Location viewportState;
    private final CompletableFuture<BufferedImage> future = new CompletableFuture<>();

    public TileJob(WeatherTile tile, Vector topLeft, Vector botRight, Location viewportState) {
        this.tile = tile;
        this.topLeft = topLeft;
        this.botRight = botRight;
        this.viewportState = viewportState;
    }

    public WeatherTile getTile() {
        return tile;
    }

    public Pair<IncompleteTile, CompletableFuture<BufferedImage>> getFuture(){
        return new Pair<>(new IncompleteTile(topLeft, botRight, viewportState, tile), future);
    }

}
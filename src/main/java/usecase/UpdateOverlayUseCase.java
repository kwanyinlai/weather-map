package usecase;

import dataaccessinterface.TileRepository;
import entity.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public final class UpdateOverlayUseCase {
    private final OverlayManager overlayManager;
    private final TileRepository tileCache;
    private final ProgramTime time;

    public UpdateOverlayUseCase(OverlayManager om, TileRepository tCache, ProgramTime time){
        this.overlayManager = om;
        this.tileCache = tCache;
        this.time = time;
    }


    public void update(Viewport vp){
        int zoom = vp.getBounedZoom();
        BoundingBox bBox = vp.calculateBBox();

        //Convert to tile coords,
        //lat lon as bounding box, convert lat lon to 0-1. //Move this to boundingbox entity?
        double bBoxLX = bBox.getTopLeft().getNormalizedLatitude();
        double bBoxRX = bBox.getBottomRight().getNormalizedLatitude();
        double bBoxLY = bBox.getTopLeft().getNormalizedLongitude();
        double bBoxRY = bBox.getBottomRight().getNormalizedLongitude();

        Vector topLeft = new Vector(bBoxLX, bBoxLY);
        Vector botRight = new Vector(bBoxRX, bBoxRY);

        //convert bounding box vecs to tile grid coords based on zoom (0-6, dimension are 2^z)
        topLeft.scale(Math.pow(2, zoom));
        botRight.scale(Math.pow(2, zoom));

        //get amount of visible tiles in both direction (vp might not be a square)
        int visibleTilesX = (int)botRight.x - (int)topLeft.x + 1; //(15.1 to 15.6, sill 1 tile visible)
        int visibleTilesY = (int)topLeft.y - (int)botRight.y + 1;

        for(int i = 0; i < visibleTilesX; i++){
            for(int j = 0; j < visibleTilesY; j++){
                TileCoords tc = new TileCoords((int)topLeft.x + i, (int)topLeft.y + j, zoom);
                WeatherTile tile = new WeatherTile(tc, this.time.getCurrentTime(), this.overlayManager.getSelected());
                BufferedImage tileImg = this.tileCache.getTileImageData(tile);
                this.overlayManager.drawTileToOverlay(topLeft, botRight, tile, tileImg);
            }
        }
        //output.setoverlay(this.overlayManager.getOverlay());
    }




}

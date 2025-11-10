package usecase.weatherLayers;

import dataaccessinterface.TileNotFoundException;
import dataaccessinterface.TileRepository;
import entity.*;

import java.awt.image.BufferedImage;

public final class UpdateOverlayUseCase {
    private final OverlayManager overlayManager;
    private final TileRepository tileCache;
    private final ProgramTime time;
    private final Viewport viewport;

    public UpdateOverlayUseCase(OverlayManager om, TileRepository tCache, ProgramTime time, Viewport vp){
        this.overlayManager = om;
        this.tileCache = tCache;
        this.time = time;
        this.viewport = vp;
    }

    //legacy NOT INTENDED FOR USE
    public UpdateOverlayUseCase(OverlayManager om, TileRepository tCache, ProgramTime time){
        this.overlayManager = om;
        this.tileCache = tCache;
        this.time = time;
        this.viewport = new Viewport(0,0,0,0,0,0,0);
    }


    public void update(){

        if (this.overlayManager.getSelectedOpacity() == 0){
            return;
        }

        int zoom = this.viewport.getBounedZoom();
        BoundingBox bBox = this.viewport.calculateBBox();

        //Convert to tile coords,
        //lat lon as bounding box, convert lat lon to 0-1. //Move this to boundingbox entity?
        double bBoxLX = bBox.getTopLeft().getNormalizedLatitude();
        double bBoxRX = bBox.getBottomRight().getNormalizedLatitude();
        double bBoxLY = bBox.getTopLeft().getNormalizedLongitude();
        double bBoxRY = bBox.getBottomRight().getNormalizedLongitude();

        Vector topLeft = new Vector(bBoxLX, 1 - bBoxLY);
        Vector botRight = new Vector(bBoxRX, 1 - bBoxRY);

        //convert bounding box vecs to tile grid coords based on zoom (0-6, dimension are 2^z)
        topLeft.scale(Math.pow(2, zoom));
        botRight.scale(Math.pow(2, zoom));

        //get amount of visible tiles in both direction (vp might not be a square)
        int visibleTilesX = (int)botRight.x - (int)topLeft.x + 1; //(15.1 to 15.6, sill 1 tile visible)
        int visibleTilesY = (int)topLeft.y - (int)botRight.y + 1;

        for(int i = 0; i < visibleTilesX; i++){
            for(int j = 0; j < visibleTilesY; j++) {
                //TODO looping? ((i % 2^zoom) + 2^zoom) % 2^zoom, j...
                int x = (int) topLeft.x + i;
                int y = (int) topLeft.y + j;

                if (x >= 0 && x < Math.pow(2, zoom) && y >= 0 && y < Math.pow(2, zoom)) {
                    TileCoords tc = new TileCoords(x, y, zoom);
                    WeatherTile tile = new WeatherTile(tc, this.time.getCurrentTime(), this.overlayManager.getSelected());
                    BufferedImage tileImg;
                    try {
                        tileImg = this.tileCache.getTileImageData(tile);
                    } catch (TileNotFoundException e) {
                        tileImg = new BufferedImage(256, 256, BufferedImage.TYPE_3BYTE_BGR);
                    }


                    this.overlayManager.drawTileToOverlay(topLeft, botRight, tile, tileImg);
                }
            }
        }
    }
        //output.setoverlay(this.overlayManager.getOverlay());
}






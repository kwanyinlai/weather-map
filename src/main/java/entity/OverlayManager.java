package entity;

import dataaccessinterface.TileNotFoundException;
import dataaccessinterface.TileRepository;
import dataaccessobjects.tilejobs.TileCompletedListener;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Arrays;

public class OverlayManager {
    private final ArrayList<WeatherType> types;
    private final ArrayList<Float> opacity;
    private WeatherType selected;
    private BufferedImage overlay;

    public OverlayManager(int x, int y){
        this.types = new ArrayList<>();
        this.types.add(WeatherType.Tmp2m);
        this.types.add(WeatherType.Precip);
        this.types.add(WeatherType.Pressure);
        this.types.add(WeatherType.Wind);
        this.opacity = new ArrayList<>(Arrays.asList((float)0.5, (float)0.5, (float)0.5, (float)0.5));
        this.selected = WeatherType.Tmp2m;
        this.overlay = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);

    }

    public void changeSize(Dimension size){
        this.overlay = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    }


    public void setSelected(WeatherType selection) throws LayerNotFoundException {
        // Change the selected layer. Raise LayerNotFoundException if selection is not an added overlay type.
        if(this.types.contains(selection)) {
            this.selected = selection;
        }
        else {throw new LayerNotFoundException(selection.toString());}
    }

    public WeatherType getSelected(){return this.selected;}

    public float getSelectedOpacity(){
        if (this.selected == null) {return 0;}
        return this.opacity.get(this.types.indexOf(this.selected));
    }

    public void setSelectedOpacity(float opacity){
        if (this.selected == null) {return;}
        this.opacity.set(this.types.indexOf(this.selected), opacity);
    }

    public BufferedImage getOverlay() {
        return overlay;
    }

    /**
    Draw given tile onto the overlay based on the viewport bounding box, represented as 2 vectors,
    in tile coordinates.
     @param tl - the top left of the viewport, in tile coordinates as a vector.
     @param br - the bottom right of the viewport, in tile coordinates as a vector.
     @param tile - the metadata of the tile
     @param tileImg - the bufferedImage of the tile
     **/
    public void drawTileToOverlay(Vector tl, Vector br, WeatherTile tile, BufferedImage tileImg){
        //
        TileCoords tc = tile.getCoordinates();
        //convert tile location to Vector for convenience.
        Vector tileCoord = new Vector(tc.getX(),tc.getY());
        Vector topLeft = new Vector(tl);
        Vector botRight = new Vector(br);

        //based on the bBox vecs' width and height, figure out where the tile should go.
        //1. move viewport and tile to top left of tile grid. i.e. subtract br, tilec by tl
        tileCoord.sub(topLeft);
        botRight.sub(topLeft);

        //2. find a value s s.t. viewport * s = overlay. Assume same porportion,
        // so br.x * s should = overlay.getWidth() and y*s = height.
        double scaleToOvl = (double)this.overlay.getWidth() / botRight.x ;

        //3. tc * c, top left of tile image is now aligned with the overlay image.
        tileCoord.scale(scaleToOvl);

        //4. scale the tile image to fit onto a tile of the overlay.
        // each tile png is 256x256.
        // (theoratical) "scale" the image to fit onto the UV tile grid, then scale by c.
        // same as c * "scale" = c / (2^zoom * 256)
        double pngToTileFactor = scaleToOvl / 256 ;
        //apply scale to the tile image.
        Image scaledTileImg = tileImg.getScaledInstance((int)(256*pngToTileFactor), -1, Image.SCALE_AREA_AVERAGING);

        //draw tile image onto overlay with selected layer's opacity.
        Graphics2D g = this.overlay.createGraphics();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC, getSelectedOpacity());
        g.setComposite(alphaComposite);
        g.drawImage(scaledTileImg, (int)tileCoord.x, (int)tileCoord.y, null);
        g.dispose();
    }
}


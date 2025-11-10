package dataaccessinterface;

import entity.WeatherTile;
import java.io.IOException;

/** Exception class when retrieval of a tile from the API is invalid
 *  because the tile input does not correspond to a valid tile.
 */
public class TileNotFoundException extends IOException {

    public TileNotFoundException(WeatherTile tile) {
        super("The parameters x=" + tile.getCoordinates().getY() + ",y="+tile.getCoordinates().getY()+
                ",zoom="+tile.getCoordinates().getZoom()+",time="+tile.getTimestamp()+" do not " +
                "correspond to a valid tile.");
    }

    public TileNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
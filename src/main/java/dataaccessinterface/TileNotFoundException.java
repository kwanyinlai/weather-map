package dataaccessinterface;

import entity.WeatherTile;
import java.io.IOException;

/** Exception class when retrieval of a tile from the API is invalid
 *  because the tile input does not correspond to a valid tile.
 */
public class TileNotFoundException extends IOException {

    public TileNotFoundException(WeatherTile tile) {
        super("The parameters x=" + tile.getCoordinates().x + ",y="+tile.getCoordinates().y+
                ",zoom="+tile.GetCoordinates().zoom+",time="+tile.getTimeStamp()+" do not " +
                "correspond to a valid tile.");
    }

    public TileNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
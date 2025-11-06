package dataaccessobjects;

import entity.WeatherTile;
import java.io.IOException;

public class TileNotFoundException extends IOException {
    public TileNotFoundException(WeatherTile tile) {
        super("The parameters x=" + tile.getCoordinates().x + ",y="+tile.getCoordinates().y+
                ",zoom="+tile.GetCoordinates().zoom+",time="+tile.getTimeStamp()+" do not " +
                "correpsond to a valid tile.");
    }

    public TileNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
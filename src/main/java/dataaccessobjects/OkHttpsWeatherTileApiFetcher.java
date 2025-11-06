package dataaccessobjects;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import dataaccessinterface.WeatherTileApiFetcher;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import entity.WeatherTile;
import javax.imageio.ImageIO;

/**
 * This WeatherTile fetcher makes a direct call to the API using OkHttps and
 * returns the image data with no caching
 */
public class OkHttpsWeatherTileApiFetcher implements WeatherTileApiFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /** Make an API call to <a href="https://weathermaps.weatherapi.com">WeatherMaps</a> API to request
     * image data, and return the image data.
     *
     * @param tile the tile for which image data is to be collected for
     * @return image data associated with <code>tile</code>
     * @throws TileNotFoundException If image data for <code>tile</code> could not be parsed, or if the tile given is invalid
     */
    public BufferedImage getWeatherTileImageData(WeatherTile tile) throws TileNotFoundException {
        String url = "https://weathermaps.weatherapi.com/";
        final Request request = new Request.Builder()
                .url(String.format("%s/%s/tiles/%s/%s/%s/%s/%s.png",
                        url,
                        tile.getWeatherType().name().toLowerCase(),
                        tile.getUtcDateAsString(),
                        tile.getUtcHourAsString(),
                        tile.getCoordinates().x,
                        tile.getCoordinates().y,
                        tile.getCoordinates().zoom))
                .build();

        try (Response response = client.newCall(request).execute()){
            if (response.code() == 404) {
                throw new TileNotFoundException(tile);
            }
            return extractImageData(response);

        } catch (IOException e) {
            throw new TileNotFoundException("Failed to read tile image data.");
        }

    }

    private BufferedImage extractImageData(Response response) throws IOException {
        try {
            assert response.body() != null;
            InputStream in = response.body().byteStream();
            BufferedImage image = ImageIO.read(in);
            if (image == null){
                throw new IOException();
            }
            return image;
        }
        catch (IOException e){
            throw new IOException();
        }
    }
}


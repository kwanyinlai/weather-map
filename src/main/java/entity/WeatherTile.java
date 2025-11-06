package entity;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class WeatherTile {
    private final TileCoords coordinates;
    private final java.time.Instant timestamp;
    private final WeatherType weatherType;

    public WeatherTile(TileCoords coordinates, java.time.Instant timestamp, WeatherType weatherType) {
        this.coordinates = coordinates;
        this.timestamp = timestamp;
        this.weatherType = weatherType;
    }

    public java.time.Instant getTimestamp() { return timestamp; }

    /** Return the UTC date of the tile
     *
     * @return String of the UTC date in yyyyMMdd format e.g: 1st Nov 2025 will be 20251101.
     */
    public String getUtcDateAsString() {
        return timestamp.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyMMdd"));
    }

    /** Return the UTC hour of the tile
     *
     * @return String of the UTC hour in 24 format. E.g: 1 am will be 01. 6 pm will be 18.
     */
    public String getUtcHourAsString() {
        return timestamp.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("HH"));
    }

    public TileCoords getCoordinates() {
        return coordinates;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public String generateKey(){
        return coordinates.x+","+coordinates.y+","+zoom+","+timestamp;
    }
}

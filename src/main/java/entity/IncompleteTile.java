package entity;

public class IncompleteTile {
    private final Vector topLeft;
    private final Vector botRight;
    private final Location viewportState;
    private final WeatherTile tile;

    public IncompleteTile(Vector topLeft, Vector botRight, Location viewportState, WeatherTile tile){
        this.topLeft = topLeft;
        this.botRight = botRight;
        this.viewportState = viewportState;
        this.tile = tile;
    }

    public Vector getTopLeft() {
        return topLeft;
    }

    public Vector getBotRight() {
        return botRight;
    }

    public WeatherTile getWeatherTile() {
        return tile;
    }

}

package entity;

public enum WeatherType {
    Tmp2m,
    Precip,
    Pressure,
    Wind;

    @Override
    public String toString() {
        switch (name()) {
            case "Tmp2m":
                return "Temperature at 2 metres";
            case "Precip":
                return "Precipitation";
            case "Pressure":
                return "Pressure";
            case "WindSpeed":
                return "Wind Speed";
            default:
                return super.toString();
        }
    }
}

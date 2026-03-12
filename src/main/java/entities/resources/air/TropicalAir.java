package entities.resources.air;

import fileio.CommandInput;

public final class TropicalAir extends Air {
    private double co2Level;
    private double rainfall = 0.0;
    private int weatherChangeTimestamp = -1;
    private static final double OXYGEN_WEIGH = 2.0;
    private static final double HUMIDITY_WEIGH = 0.5;
    private static final double CO2_WEIGH = 0.01;
    private static final double RAINFALL_WEIGH = 0.3;
    private static final double MAX_SCORE = 82.0;
    public TropicalAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel, final double co2Level) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
    }
    @Override
    public double airQuality() {
        double oxygenLevel = getOxygenLevel();
        double humidity = getHumidity();
        double score = (oxygenLevel * OXYGEN_WEIGH) + (humidity * HUMIDITY_WEIGH)
                - (co2Level * CO2_WEIGH);
        if (rainfall != 0.0) {
            score += (rainfall * HUMIDITY_WEIGH);
        }
        return normalizeAndRoundScore(score);
    }
    @Override
    public double maxAirQualityScore() {
        return MAX_SCORE;
    }
    @Override
    public String getType() {
        return "TropicalAir";
    }

    @Override
    public double getExtraField() {
        return normalizeAndRoundScore(co2Level);
    }

    @Override
    public String getExtraFieldName() {
        return "co2Level";
    }
    public void setRainfall(final double rainfall) {
        this.rainfall = rainfall;
    }
    @Override
    public void changeWeather(final CommandInput commandInput) {
        if (commandInput.getType().equals("rainfall")) {
            this.setRainfall(commandInput.getRainfall());
            this.weatherChangeTimestamp = commandInput.getTimestamp();
        }
    }

    @Override
    public void updateWeather(final int timestamp) {
        if (weatherChangeTimestamp != -1 && timestamp >= weatherChangeTimestamp + 2) {
            this.rainfall = 0.0;
            this.weatherChangeTimestamp = -1;
        }

    }
}

package entities.resources.air;

import fileio.CommandInput;

public final class TemperateAir extends Air {
    private double pollenLevel;
    private String season = "";
    private int weatherChangeTimestamp = -1;
    private static final double OXYGEN_WEIGH = 2.0;
    private static final double HUMIDITY_WIEGH = 0.7;
    private static final double POLLEN_WEIGH = 0.1;
    private static final int SEASON_PENALTY = 15;
    private static final double MAX_QUAL = 84.0;
    public TemperateAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel,
                        final double pollenLevel) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
    }
    @Override
    public double airQuality() {
        double oxygenLevel = getOxygenLevel();
        double humidity = getHumidity();
        double score = (oxygenLevel * OXYGEN_WEIGH) + (humidity * HUMIDITY_WIEGH)
                - (pollenLevel * POLLEN_WEIGH);
        if (season != null) {
            double seasonPenalty = season.equalsIgnoreCase("Spring") ? SEASON_PENALTY : 0;
            score -= seasonPenalty;
        }
        return normalizeAndRoundScore(score);
    }
    @Override
    public double maxAirQualityScore() {
        return MAX_QUAL;
    }

    @Override
    public String getType() {
        return "TemperateAir";
    }

    @Override
    public double getExtraField() {
        return pollenLevel;
    }

    @Override
    public String getExtraFieldName() {
        return "pollenLevel";
    }
    public void setNewSeason(final String newSeason) {
        this.season = newSeason;
    }
    @Override
    public void changeWeather(final CommandInput commandInput) {
        if (commandInput.getType().equals("newSeason")) {
            this.setNewSeason(commandInput.getSeason());
            this.weatherChangeTimestamp = commandInput.getTimestamp();
        }
    }
    @Override
    public void updateWeather(final int timestamp) {
        if (weatherChangeTimestamp != -1 && timestamp >= weatherChangeTimestamp + 2) {
            this.season = "";
            this.weatherChangeTimestamp = -1;
        }
    }
}

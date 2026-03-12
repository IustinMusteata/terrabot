package entities.resources.air;

import fileio.CommandInput;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class DesertAir extends Air {
    private double dustParticles;
    private boolean desertStorm = false;
    private int weatherChangeTimestamp = -1;
    private static final double OXYGEN_WEIGH = 2.0;
    private static final double DUST_WEIGH = 0.2;
    private static final double TEMP_WEIGH = 0.3;
    private static final int STORM_PENALTY = 30;
    private static final double MAX_SCORE = 65.0;
    public DesertAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel,
                     final double dustParticles) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
    }
    @Override
    public double airQuality() {
        double oxygenLevel = getOxygenLevel();
        double temperature = getTemperature();
        double score = (oxygenLevel * OXYGEN_WEIGH)
                - (dustParticles * DUST_WEIGH) - (temperature * TEMP_WEIGH);
        if (this.desertStorm) {
            score -= (desertStorm ? STORM_PENALTY : 0);
        }
        return normalizeAndRoundScore(score);
    }
    @Override
    public double maxAirQualityScore() {
        return MAX_SCORE;
    }
    @Override
    public String getType() {
        return "DesertAir";
    }
    @Override
    public double getExtraField() {
        return dustParticles;
    }
    @Override
    public String getExtraFieldName() {
        return "desertStorm";
    }
    public void setDesertStorm(final boolean desertStorm) {
        this.desertStorm = desertStorm;
    }
    public boolean getDesertStorm() {
        return this.desertStorm;
    }
    @Override
    public void changeWeather(final CommandInput commandInput) {
        if (commandInput.getType().equals("desertStorm")) {
            this.setDesertStorm(commandInput.isDesertStorm());
            this.weatherChangeTimestamp = commandInput.getTimestamp();
        }
    }

    @Override
    public void updateWeather(final int timestamp) {
        if (weatherChangeTimestamp != -1 && timestamp >= weatherChangeTimestamp + 2) {
            this.desertStorm = false;
            this.weatherChangeTimestamp = -1;
        }
    }

    @Override
    public void getWeatherInfo(final ObjectNode node) {
        node.put("desertStorm", this.getDesertStorm());
    }
}

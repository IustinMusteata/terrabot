package entities.resources.air;

import fileio.CommandInput;

public final class PolarAir extends Air {
    private double iceCrystalConcentration;
    private double windSpeed = 0.0;
    private int weatherChangeTimestamp = -1;
    private static final double OXYGEN_WEIGH = 2.0;
    private static final double WIND_WEIGH = 0.2;
    private static final double ICE_WEIGH = 0.05;
    private static final int MAX_SCORE = 100;
    private static final double MAX_QUAL = 142.0;

    public static double getMaxQual() {
        return MAX_QUAL;
    }

    public PolarAir(final String name, final double mass, final double humidity,
                    final double temperature, final double oxygenLevel,
                    final double iceCrystalConcentration) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }
    @Override
    public double airQuality() {
        double oxygenLevel = getOxygenLevel();
        double temperature = getTemperature();
        double score = (oxygenLevel * OXYGEN_WEIGH) + (MAX_SCORE - Math.abs(temperature))
                - (iceCrystalConcentration * ICE_WEIGH);
        if (windSpeed != 0.0) {
            score -= (windSpeed * WIND_WEIGH);
        }
        return normalizeAndRoundScore(score);
    }
    @Override
    public double maxAirQualityScore() {
        return MAX_QUAL;
    }

    @Override
    public String getType() {
        return "PolarAir";
    }

    @Override
    public double getExtraField() {
        return iceCrystalConcentration;
    }

    @Override
    public String getExtraFieldName() {
        return "iceCrystalConcentration";
    }
    public void setWindSpeed(final double windSpeed) {
        this.windSpeed = windSpeed;
    }
    @Override
    public void changeWeather(final CommandInput commandInput) {
        if (commandInput.getType().equals("polarStorm")) {
            this.setWindSpeed(commandInput.getWindSpeed());
            this.weatherChangeTimestamp = commandInput.getTimestamp();
        }
    }

    @Override
    public void updateWeather(final int timestamp) {
        if (weatherChangeTimestamp != -1 && timestamp >= weatherChangeTimestamp + 2) {
            this.windSpeed = 0.0;
            this.weatherChangeTimestamp = -1;
        }
    }
}

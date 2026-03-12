package entities.resources.air;

import fileio.CommandInput;

public final class MountainAir extends Air {
    private double altitude;
    private int numberOfHikers = 0;
    private int weatherChangeTimestamp = -1;
    private static final double OXYGEN_WEIGH = 2.0;
    private static final double HUMIDITY_WEIGH = 0.6;
    private static final double ALTITUDE_DIV = 1000.0;
    private static final double ALTITUDE_FACT = 0.5;
    private static final double HIKER_WEIGH = 0.1;
    private static final double MAX_QUAL = 78.0;
    public MountainAir(final String name, final double mass, final double humidity,
                     final double temperature, final double oxygenLevel, final double altitude) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.altitude = altitude;
    }
    @Override
    public double airQuality() {
        double oxygenLevel = getOxygenLevel();
        double humidity = getHumidity();
        double oxygenFactor = oxygenLevel - (altitude / ALTITUDE_DIV * ALTITUDE_FACT);
        double score = (oxygenFactor * OXYGEN_WEIGH) + (humidity * HUMIDITY_WEIGH);
        if (numberOfHikers != 0) {
            score -= (numberOfHikers * HIKER_WEIGH);
        }
        return normalizeAndRoundScore(score);
    }
    @Override
    public double maxAirQualityScore() {
        return MAX_QUAL;
    }
    @Override
    public String getType() {
        return "MountainAir";
    }
    @Override
    public double getExtraField() {
        return altitude;
    }
    @Override
    public String getExtraFieldName() {
        return "altitude";
    }
    public void setNumberOfHikers(final int numberOfHikers) {
        this.numberOfHikers = numberOfHikers;
    }
    @Override
    public void changeWeather(final CommandInput commandInput) {
        if (commandInput.getType().equals("peopleHiking")) {
            this.setNumberOfHikers(commandInput.getNumberOfHikers());
            this.weatherChangeTimestamp = commandInput.getTimestamp();
        }
    }

    @Override
    public void updateWeather(final int timestamp) {
        if (weatherChangeTimestamp != -1 && timestamp >= weatherChangeTimestamp + 2) {
            this.setNumberOfHikers(0);
            this.weatherChangeTimestamp = -1;
        }
    }
}

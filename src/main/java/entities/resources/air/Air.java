package entities.resources.air;

import entities.Entity;
import fileio.CommandInput;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Air extends Entity {
    private double humidity;
    private double temperature;
    private double oxygenLevel;
    private static final int POOR_QUAL = 40;
    private static final int MODERATE_QUAL = 70;
    private static final int MAX_QUAL = 100;
    private static final double MAX_SCORE = 100.0;
    private static final double TOXIC_SCORE = 0.8;
    public Air(final String name, final double mass, final double humidity,
               final double temperature, final double oxygenLevel) {
        super(name, mass);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }
    public final double getHumidity() {
        return humidity;
    }
    public final double getTemperature() {
        return temperature;
    }
    public final double getOxygenLevel() {
        return oxygenLevel;
    }

    /**
     * returns Air Quality, based on calculations for each type of Air
     * Implemented in each of the subclasses.
     * @return air quality as a double
     */
    public abstract double airQuality();

    /**
     * returns the max score for each type of air, implemented in each of
     * the subclasses
     * @return max score as a double
     */
    public abstract double maxAirQualityScore();

    /**
     * returns type of Air, implemented in each of the subclasses
     * @return type of Air, as a string
     */
    public abstract String getType();

    /**
     * returns the extra field that each type of Air has
     * implemented in each of the subclasses
     * @return extra field, as a double
     */
    public abstract double getExtraField();

    /**
     * retuns the name of this extra field, that each type of Air has
     * implemented in each of the subclasses
     * @return name of the extra field, as a string
     */
    public abstract String getExtraFieldName();

    /**
     * Implements the weather changes, based on events given by input
     * implemented in each subclass, as each type of air has its specific event that
     * affects its weather
     * @param commandInput given weather event
     */
    public abstract void changeWeather(CommandInput commandInput);

    /**
     * updates the weather based on timestamps,
     * as weather events change their status based on timestamps
     * implemented in each subclass
     * @param timestamp given timestamp that is to be compared with
     *                  weatherChangeTimestamp
     */
    public abstract void updateWeather(int timestamp);

    /**
     * Used to get weather info for DesertAir, made
     * for printing desertStorm when necessary.
     * overriden only in DesertAir, hence being written here
     * as { }
     * @param node node where the output necessary has to be put
     */
    public void getWeatherInfo(final ObjectNode node) { }

    /**
     * Returns in what category does the air quality belong
     * Poor quality if airquality under 40, Moderate if under 70,
     * Good if bigger than 70
     * @return poor/moderate/good (wrong calculation used for debugging)
     */
    public final String getAirQuality() {
        if (airQuality() < POOR_QUAL) {
            return "poor";
        } else if (airQuality() < MODERATE_QUAL) {
            return "moderate";
        } else if (airQuality() <= MAX_QUAL) {
            return "good";
        } else {
            return "wrong calculation";
        }

    }

    /**
     * Calculates the air's toxicity level based on formula given.
     * @return air toxicity level as a double
     */
    public final double airToxicity() {
        double airQuality = normalizeAndRoundScore(airQuality());
        double maxAirQualityScore = maxAirQualityScore();
        double toxicityAQ = MAX_QUAL * (1 - airQuality / maxAirQualityScore);
        return (Math.round(Math.max(0, toxicityAQ) * MAX_SCORE) / MAX_SCORE);
    }

    /**
     * Checks if air is considered toxic, based on criteria(if air toxicity
     * bigger than 0.8 * max score for each type of air.
     * @return true if air is considered toxic, false otherwise
     */
    public final boolean isAirToxic() {
        return airToxicity() > (TOXIC_SCORE * maxAirQualityScore());
    }

    /**
     * Sets the oxygenLevel, by getting the oxygenlevel as a parameter
     * and then normalizing and rounding it.
     * @param oxygenLevel the level of oxygen that is to be set in this current instance
     */
    public final void setOxygenLevel(final double oxygenLevel) {
        double oxygenLevel2 = Math.max(0, oxygenLevel);
        this.oxygenLevel = Math.round(oxygenLevel * MAX_SCORE) / MAX_SCORE;
    }

    /**
     * Sets humidity at given level, that is normalized and rounded
     * @param humidity humidity level that is to be set in this current instance
     */
    public final void setHumidity(final double humidity) {
        this.humidity = normalizeAndRoundScore(humidity);
    }
}

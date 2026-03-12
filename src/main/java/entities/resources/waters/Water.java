package entities.resources.waters;

import entities.Entity;

public abstract class Water extends Entity {
    private double salinity;
    private double pH;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;
    private static final double MAX_SCORE = 100.0;
    private static final double IDEAL_PH = 7.5;
    private static final double MAX_SALINITY = 350.0;
    private static final double PURITY_WEIGH = 0.3;
    private static final double PH_WEIGH = 0.2;
    private static final double SALINITY_WEIGH = 0.15;
    private static final double TURBIDITY_WEIGH = 0.1;
    private static final double CONTAMINANT_WEIGH = 0.15;
    private static final double FROZEN_WEIGH = 0.2;
    private static final int POOR_QUAL = 40;
    private static final int MODERATE_QUAL = 70;
    private static final int MAX_QUAL = 100;
    public Water(final String name, final double mass, final double salinity, final double pH,
                 final double purity, final double turbidity,
                 final double contaminantIndex, final boolean isFrozen) {
        super(name, mass);
        this.salinity = salinity;
        this.pH = pH;
        this.purity = purity;
        this.turbidity = turbidity;
        this.contaminantIndex = contaminantIndex;
        this.isFrozen = isFrozen;
    }

    /**
     * Calculates the water quality based on the scores given
     * in the homework's statement
     * @return waterQuality as a double
     */
    public final double waterQuality() {
        double purityScore = purity / MAX_SCORE;
        double phScore = 1 - (Math.abs(pH - IDEAL_PH) / IDEAL_PH);
        double salinityScore = 1 - (salinity / MAX_SALINITY);
        double turbidityScore = 1 - (turbidity / MAX_SCORE);
        double contaminantScore = 1 - (contaminantIndex / MAX_SCORE);
        double frozenScore = isFrozen ? 0 : 1;
        double waterQuality = (PURITY_WEIGH * purityScore + PH_WEIGH * phScore
                + SALINITY_WEIGH * salinityScore + TURBIDITY_WEIGH * turbidityScore
                + CONTAMINANT_WEIGH * contaminantScore + FROZEN_WEIGH * frozenScore) * MAX_QUAL;
        return waterQuality;
    }

    /**
     * Returns in what category the water quality is in.
     * If under 40, quality is poor, if under 70, moderate, and if higher than that,
     * quality is good.
     * @return Poor/Moderate/Good (Wrong calculation was for debugging)
     */
    public final String getWaterQuality() {
        if (waterQuality() < POOR_QUAL) {
            return "poor";
        } else if (waterQuality() < MODERATE_QUAL) {
            return "moderate";
        } else if (waterQuality() <= MAX_QUAL) {
            return "good";
        } else {
            return "Wrong calculation";
        }
    }

    /**
     * Implements the process of drinking water, substracting
     * from mass the amount of water that was drank
     * @param waterAmount the amount of water that was drank
     *                    and needs to be substracted from mass
     */
    public final void drinkWater(final double waterAmount) {
        double mass = getMass();
        if (waterAmount >= mass) {
            setMass(0);
        } else {
            setMass(mass - waterAmount);
        }
    }

    /**
     * Returns the type of water, method implemented in each subclass with its respective
     * type
     * @return type of water as a string
     */
    public abstract String getType();



}

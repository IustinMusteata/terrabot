package entities.plants;

import entities.Entity;

public abstract class Plant extends Entity {
    private LifeStage currentLifeStage;
    private double maturityGrowth;
    private double oxygenFromPlant;
    private int plantPossibility;
    private static final double MAX_SCORE = 100.0;

    public Plant(final String name, final double mass,
                 final double oxygenFromPlant, final int plantPossibility) {
        super(name, mass);
        this.oxygenFromPlant = oxygenFromPlant;
        this.plantPossibility = plantPossibility;
        this.currentLifeStage = LifeStage.YOUNG;
        this.maturityGrowth = 0.0;
    }

    public final LifeStage getCurrentLifeStage() {
        return currentLifeStage;
    }

    public final double getOxygenFromPlant() {
        return oxygenFromPlant;
    }

    public final double getPlantPossibility() {
        return plantPossibility / MAX_SCORE;
    }

    /**
     * Returns oxygenLevel, by adding the base oxygen from plant and the Oxygen given by the
     * plant's maturity rate. IF plant is dead, oxygenlevel is 0.
     * @return oxygen level, 0 if plant dead, sum of oxygen from plant and rate of oxygen from
     * maturity stage
     */
    public final double getOxygenLevel() {
        if (currentLifeStage == LifeStage.DEAD) {
            return 0.0;
        }
        return this.oxygenFromPlant + this.currentLifeStage.getMaturityOxygenRate();
    }

    /**
     * Changes the plant's maturity level based on its growth.
     * The method adds the given oxygenAmount to the plant's
     * maturity growth counter.
     * @param oxygenAmount the amount of oxygen which is
     *                     contributing to the plant's
     *                     maturity growth
     */
    public final void growMaturityLevel(final double oxygenAmount) {
        if (currentLifeStage == LifeStage.DEAD) {
            return;
        }
        this.maturityGrowth += oxygenAmount;
        if (this.maturityGrowth >= 1.0) {
            if (currentLifeStage == LifeStage.YOUNG) {
                currentLifeStage = LifeStage.MATURE;
            } else if (currentLifeStage == LifeStage.MATURE) {
                currentLifeStage = LifeStage.OLD;
            } else if (currentLifeStage == LifeStage.OLD) {
                currentLifeStage = LifeStage.DEAD;
            }
            this.maturityGrowth = 0.0;
        }
    }

    /**
     * Get type of plant. implemented in each subclass with its respective type
     * @return type of plant as a string.
     */
    public abstract String getType();
}

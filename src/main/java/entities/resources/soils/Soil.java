package entities.resources.soils;

import entities.Entity;

public abstract class Soil extends Entity {
    private double nitrogen;
    private double waterRetention;
    private double soilpH;
    private double organicMatter;
    private static final double WATER_ABSORBTION_RATE = 0.1;
    private static final int POOR_QUAL = 40;
    private static final int MODERATE_QUAL = 70;
    private static final int MAX_QUAL = 100;
    private static final double MAX_SCORE = 100.0;
    public Soil(final String name, final double mass, final double nitrogen,
                final double waterRetention, final double soilpH, final double organicMatter) {
        super(name, mass);
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
    }

    public final double getNitrogen() {
        return nitrogen;
    }
    public final double getWaterRetention() {
        return waterRetention;
    }
    public final double getSoilpH() {
        return soilpH;
    }
    public final double getOrganicMatter() {
        return organicMatter;
    }
    public abstract double soilQuality();
    public abstract double soilPossibility();
    public abstract String getType();
    public abstract double getExtraField();
    public abstract String getExtraFieldName();
    public final String getSoilQuality() {
        if (soilQuality() < POOR_QUAL) {
            return "poor";
        } else if (soilQuality() < MODERATE_QUAL) {
            return "moderate";
        } else if (soilQuality() <= MAX_QUAL) {
            return "good";
        } else {
            return "wrong calculation";
        }
    }
    public final void waterAbsorbtion() {
        this.waterRetention += WATER_ABSORBTION_RATE;
    }
    public final void addOrganicMatter(final double amount) {
        this.organicMatter += amount;
        this.organicMatter = Math.round(this.organicMatter * MAX_SCORE) / MAX_SCORE;
    }
    public final void setWaterRetention(final double waterRetention) {
        this.waterRetention = normalizeAndRoundScore(waterRetention);
    }
}

package entities.resources.soils;

public final class GrasslandSoil extends Soil {
    private double rootDensity;
    private static final double NITROGEN_WEIGH = 1.3;
    private static final double ORGANIC_MATTER_WEIGH = 1.5;
    private static final double ROOTDENSITY_WEIGH = 0.8;
    private static final double BASE = 50.0;
    private static final double WATER_RETENTION_FACT = 0.5;
    private static final double BASE_DIV = 75.0;
    private static final double MAX_SCORE = 100.0;
    public GrasslandSoil(final String name, final double mass,
                         final double nitrogen, final double waterRetention,
                         final double soilpH, final double organicMatter,
                         final double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.rootDensity = rootDensity;
    }
    @Override
    public double soilQuality() {
        double nitrogen = getNitrogen();
        double organicMatter = getOrganicMatter();
        double score = (nitrogen * NITROGEN_WEIGH)
                + (organicMatter * ORGANIC_MATTER_WEIGH)
                + (rootDensity * ROOTDENSITY_WEIGH);
        return normalizeAndRoundScore(score);
    }
    @Override
    public double soilPossibility() {
        double waterRetention = getWaterRetention();
        return ((BASE - this.rootDensity + waterRetention * WATER_RETENTION_FACT)
                / BASE_DIV * MAX_SCORE);
    }
    @Override
    public String getType() {
        return "GrasslandSoil";
    }
    @Override
    public double getExtraField() {
        return rootDensity;
    }
    @Override
    public String getExtraFieldName() {
        return "rootDensity";
    }
}

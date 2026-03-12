package entities.resources.soils;

public final class SwampSoil extends Soil {
    private double waterLogging;
    private static final double NITROGEN_WEIGH = 1.1;
    private static final double ORGANIC_MATTER_WEIGH = 2.2;
    private static final double WATER_LOGGING_WEIGH = 5.0;
    private static final double POSSIBILITY_MULTIPLER = 10.0;
    public SwampSoil(final String name, final double mass,
                     final double nitrogen, final double waterRetention,
                     final double soilpH, final double organicMatter, final double waterLogging) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.waterLogging = waterLogging;
    }
    @Override
    public double soilQuality() {
        double nitrogen = getNitrogen();
        double organicMatter = getOrganicMatter();
        double score = (nitrogen * NITROGEN_WEIGH) + (organicMatter * ORGANIC_MATTER_WEIGH)
                - (waterLogging * WATER_LOGGING_WEIGH);
        return normalizeAndRoundScore(score);
    }
    @Override
    public double soilPossibility() {
        return (this.waterLogging * POSSIBILITY_MULTIPLER);
    }
    @Override
    public String getType() {
        return "SwampSoil";
    }
    @Override
    public double getExtraField() {
        return waterLogging;
    }
    @Override
    public String getExtraFieldName() {
        return "waterLogging";
    }
}

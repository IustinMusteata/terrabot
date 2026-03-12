package entities.resources.soils;

public final class TundraSoil extends Soil {
    private double permafrostDepth;
    private static final double NITROGEN_WEIGH = 0.7;
    private static final double ORGANIC_MATTER_WEIGH = 0.5;
    private static final double PERMAFROST_WEIGH = 1.5;
    private static final double BASE_POSSIBILITY = 50.0;
    private static final double POSSIBILITY_DIV = 50.0;
    private static final double MAX_SCORE = 100.0;
    public TundraSoil(final String name, final double mass,
                      final double nitrogen, final double waterRetention,
                         final double soilpH, final double organicMatter,
                      final double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.permafrostDepth = permafrostDepth;
    }
    @Override
    public double soilQuality() {
        double nitrogen = getNitrogen();
        double organicMatter = getOrganicMatter();
        double score = (nitrogen * NITROGEN_WEIGH) + (organicMatter * ORGANIC_MATTER_WEIGH)
                - (permafrostDepth * PERMAFROST_WEIGH);
        return normalizeAndRoundScore(score);
    }

    @Override
    public double soilPossibility() {
        return ((BASE_POSSIBILITY - permafrostDepth) / POSSIBILITY_DIV * MAX_SCORE);
    }
    @Override
    public String getType() {
        return "TundraSoil";
    }
    @Override
    public double getExtraField() {
        return permafrostDepth;
    }
    @Override
    public String getExtraFieldName() {
        return "permafrostDepth";
    }
}

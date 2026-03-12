package entities.resources.soils;

public final class ForestSoil extends Soil {
    private double leafLitter;
    private static final double NITROGEN_WEIGH = 1.2;
    private static final int ORGANICMATTER_WEIGH = 2;
    private static final double WATER_RETENTION_WEIGH = 1.5;
    private static final double LEAF_LITTER_WEIGH = 0.3;
    private static final double WATER_RETENTION_POSSIBILITY = 0.6;
    private static final double LEAF_LITTER_POSSBILITY = 0.4;
    private static final int POSSIBILITY_DIV = 80;
    private static final int POSSBILITY_MAX = 100;
    public ForestSoil(final String name, final double mass,
                      final double nitrogen, final double waterRetention,
                      final double soilpH, final double organicMatter, final double leafLitter) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.leafLitter = leafLitter;
    }
    @Override
    public double soilQuality() {
        double nitrogen = getNitrogen();
        double organicMatter = getOrganicMatter();
        double waterRetention = getWaterRetention();
        double score = (nitrogen * NITROGEN_WEIGH) + (organicMatter * ORGANICMATTER_WEIGH)
                + (waterRetention * WATER_RETENTION_WEIGH) + (leafLitter * LEAF_LITTER_WEIGH);
        return normalizeAndRoundScore(score);
    }
    @Override
    public double soilPossibility() {
        double waterRetention = getWaterRetention();
        return ((waterRetention * WATER_RETENTION_POSSIBILITY
                + leafLitter * LEAF_LITTER_POSSBILITY) / POSSIBILITY_DIV * POSSBILITY_MAX);
    }
    @Override
    public String getType() {
        return "ForestSoil";
    }
    @Override
    public double getExtraField() {
        return leafLitter;
    }
    @Override
    public String getExtraFieldName() {
        return "leafLitter";
    }
}

package entities.resources.soils;

public final class DesertSoil extends Soil {
    private double salinity;
    private static final double NITROGEN_WEIGH = 0.5;
    private static final double WATER_RETENTION_WEIGH = 0.3;
    private static final double SALINITY_PENALTY = 2.0;
    private static final double MAX_POSSIBILITY = 100.0;
    public DesertSoil(final String name, final double mass,
                      final double nitrogen, final double waterRetention,
                      final double soilpH, final double organicMatter, final double salinity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.salinity = salinity;
    }
    @Override
    public double soilQuality() {
        double nitrogen = getNitrogen();
        double waterRetention = getWaterRetention();
        double score = (nitrogen * NITROGEN_WEIGH) + (waterRetention * WATER_RETENTION_WEIGH)
                - (salinity * SALINITY_PENALTY);
        return normalizeAndRoundScore(score);
    }
    @Override
    public double soilPossibility() {
        double waterRetention = getWaterRetention();
        return ((MAX_POSSIBILITY - waterRetention + salinity) / MAX_POSSIBILITY * MAX_POSSIBILITY);
    }
    @Override
    public String getType() {
        return "DesertSoil";
    }
    @Override
    public double getExtraField() {
        return salinity;
    }
    @Override
    public String getExtraFieldName() {
        return "salinity";
    }
}

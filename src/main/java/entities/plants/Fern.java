package entities.plants;

public final class Fern extends Plant {
    private static final double DEFAULT_OXYGEN_LVL = 0.0;
    private static final int PLANT_POSSIBILITY = 30;
    public Fern(final String name, final double mass) {
        super(name, mass, DEFAULT_OXYGEN_LVL, PLANT_POSSIBILITY);
    }
    @Override
    public String getType() {
        return "Ferns";
    }
}

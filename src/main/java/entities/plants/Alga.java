package entities.plants;

public final class Alga extends Plant {
    private static final double DEFAULT_OXYGEN_LVL = 0.5;
    private static final int PLANT_POSSIBILITY = 20;
    public Alga(final String name, final double mass) {
        super(name, mass, DEFAULT_OXYGEN_LVL, PLANT_POSSIBILITY);
    }
    @Override
    public String getType() {
        return "Algae";
    }
}

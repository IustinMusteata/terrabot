package entities.plants;

public final class Moss extends Plant {
    private static final double DEFAULT_OXYGEN_LVL = 0.8;
    private static final int PLANT_POSSIBILITY = 40;
    public Moss(final String name, final double mass) {
        super(name, mass,  DEFAULT_OXYGEN_LVL, PLANT_POSSIBILITY);
    }
    @Override
    public String getType() {
        return "Mosses";
    }
}

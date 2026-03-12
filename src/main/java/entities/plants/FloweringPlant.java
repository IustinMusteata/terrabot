package entities.plants;

public final class FloweringPlant extends Plant {
    private static final double DEFAULT_OXYGEN_LVL = 6.0;
    private static final int PLANT_POSSIBILITY = 90;
    public FloweringPlant(final String name, final double mass) {
        super(name, mass, DEFAULT_OXYGEN_LVL, PLANT_POSSIBILITY);
    }
    @Override
    public String getType() {
        return "FloweringPlants";
    }
}

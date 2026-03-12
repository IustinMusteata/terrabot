package entities.animals;

public final class Herbivore extends Animal {
    private static final double MAX_POSSIBILITY = 100.0;
    private static final double HERBIVORE_POSSIBILITY = 85.0;
    private static final double POSSBILITY_DIV = 10.0;
    public Herbivore(final String name, final double mass, final int x, final int y) {
        super(name, mass, x, y);
    }
    @Override
    public double getAttackPossibility() {
        return (MAX_POSSIBILITY - HERBIVORE_POSSIBILITY) / POSSBILITY_DIV;
    }
    @Override
    public boolean checkIfAnimalisCarnivoreOrParasite() {
        return false;
    }
    @Override
    public String getType() {
        return "Herbivores";
    }
}

package entities.animals;

public final class Parasite extends Animal {
    private static final double MAX_POSSIBILITY = 100.0;
    private static final double PARASITE_POSSIBILITY = 10.0;
    private static final double POSSBILITY_DIV = 10.0;
    public Parasite(final String name, final double mass, final int x, final int y) {
        super(name, mass, x, y);
    }
    @Override
    public double getAttackPossibility() {
        return (MAX_POSSIBILITY - PARASITE_POSSIBILITY) / POSSBILITY_DIV;
    }
    @Override
    public boolean checkIfAnimalisCarnivoreOrParasite() {
        return true;
    }
    @Override
    public String getType() {
        return "Parasites";
    }
}

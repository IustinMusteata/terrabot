package entities.animals;

public final class Detritivore extends Animal {
    private static final double MAX_POSSIBILITY = 100.0;
    private static final double DETRITIVORE_POSSIBILITY = 90.0;
    private static final double POSSBILITY_DIV = 10.0;
    public Detritivore(final String name, final double mass, final int x, final int y) {
        super(name, mass, x, y);
    }
    @Override
    public double getAttackPossibility() {
        return (MAX_POSSIBILITY - DETRITIVORE_POSSIBILITY) / POSSBILITY_DIV;
    }

    @Override
    public boolean checkIfAnimalisCarnivoreOrParasite() {
        return false;
    }
    @Override
    public String getType() {
        return "Detritivores";
    }
}

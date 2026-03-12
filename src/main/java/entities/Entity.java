package entities;

public class Entity {
    private String name;
    private double mass;
    private static final double MAX_SCORE = 100.0;
    private static final int MAX_SCORE_INT = 100;

    public Entity(final String name, final double mass) {
        this.name = name;
        this.mass = mass;
    }
    public final String getName() {
        return name;
    }
    public final double getMass() {
        return mass;
    }
    public final void setMass(final double mass) {
        this.mass = mass;
    }
    public final void setName(final String name) {
        this.name = name;
    }
    public final double normalizeAndRoundScore(final double score) {
        double normalizedScore = Math.max(0, Math.min(MAX_SCORE_INT, score));
        return (Math.round(normalizedScore * MAX_SCORE) / MAX_SCORE);
    }
}

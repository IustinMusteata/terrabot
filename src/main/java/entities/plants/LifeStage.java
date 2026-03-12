package entities.plants;

public enum LifeStage {
    YOUNG(0.2),
    MATURE(0.7),
    OLD(0.4),
    DEAD(0.0);
    private final double maturityOxygenRate;
    LifeStage(final double maturityOxygenRate) {
        this.maturityOxygenRate = maturityOxygenRate;
    }
    public double getMaturityOxygenRate() {
        return maturityOxygenRate;
    }
}

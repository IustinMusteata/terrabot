package entities.resources.waters;

public final class River extends Water {
    public River(final String name, final double mass, final double salinity, final double pH,
                 final double purity, final double turbidity,
                 final double contaminantIndex, final boolean isFrozen) {
        super(name, mass, salinity, pH, purity, turbidity, contaminantIndex, isFrozen);
    }
    @Override
    public String getType() {
        return "river";
    }
}

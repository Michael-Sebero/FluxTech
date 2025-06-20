package gregtech.api.unification.material.properties;

public class PropertyKey<T extends IMaterialProperty> {

    public static final PropertyKey<BlastProperty> BLAST = new PropertyKey<>("blast", BlastProperty.class);
    public static final PropertyKey<CoolantProperty> COOLANT = new PropertyKey<>("coolant", CoolantProperty.class);
    public static final PropertyKey<DustProperty> DUST = new PropertyKey<>("dust", DustProperty.class);
    public static final PropertyKey<FluidPipeProperties> FLUID_PIPE = new PropertyKey<>("fluid_pipe",
            FluidPipeProperties.class);
    public static final PropertyKey<FluidProperty> FLUID = new PropertyKey<>("fluid", FluidProperty.class);
    public static final PropertyKey<GemProperty> GEM = new PropertyKey<>("gem", GemProperty.class);
    public static final PropertyKey<IngotProperty> INGOT = new PropertyKey<>("ingot", IngotProperty.class);
    public static final PropertyKey<PolymerProperty> POLYMER = new PropertyKey<>("polymer", PolymerProperty.class);
    public static final PropertyKey<ItemPipeProperties> ITEM_PIPE = new PropertyKey<>("item_pipe",
            ItemPipeProperties.class);
    public static final PropertyKey<OreProperty> ORE = new PropertyKey<>("ore", OreProperty.class);
    public static final PropertyKey<MaterialToolProperty> TOOL = new PropertyKey<>("tool", MaterialToolProperty.class);
    public static final PropertyKey<ExtraToolProperty> EXTRATOOL = new PropertyKey<>("extra_tool",
            ExtraToolProperty.class);
    public static final PropertyKey<RotorProperty> ROTOR = new PropertyKey<>("rotor", RotorProperty.class);
    public static final PropertyKey<WireProperties> WIRE = new PropertyKey<>("wire", WireProperties.class);
    public static final PropertyKey<WoodProperty> WOOD = new PropertyKey<>("wood", WoodProperty.class);
    public static final PropertyKey<FissionFuelProperty> FISSION_FUEL = new PropertyKey<>("fission_fuel",
            FissionFuelProperty.class);
    public static final PropertyKey<RadioactiveProperty> RADIOACTIVE = new PropertyKey<>("radioactive",
            RadioactiveProperty.class);

    // Empty property used to allow property-less Materials without removing base type enforcement
    public static final PropertyKey<EmptyProperty> EMPTY = new PropertyKey<>("empty", EmptyProperty.class);

    private final String key;
    private final Class<T> type;

    public PropertyKey(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    protected String getKey() {
        return key;
    }

    protected T constructDefault() {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public T cast(IMaterialProperty property) {
        return this.type.cast(property);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PropertyKey) {
            return ((PropertyKey<?>) o).getKey().equals(key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key;
    }

    private static class EmptyProperty implements IMaterialProperty {

        @Override
        public void verifyProperty(MaterialProperties properties) {
            // no-op
        }
    }
}

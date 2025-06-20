package gregtech.api.unification.material.properties;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;

import java.util.Objects;

import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_FOIL;

public class WireProperties implements IMaterialProperty {

    private int voltage;
    private int amperage;
    private int lossPerBlock;
    private int superconductorCriticalTemperature;
    private boolean isSuperconductor;

    public WireProperties(int voltage, int baseAmperage, int lossPerBlock) {
        this(voltage, baseAmperage, lossPerBlock, false);
    }

    public WireProperties(int voltage, int baseAmperage, int lossPerBlock, boolean isSuperCon) {
        this(voltage, baseAmperage, lossPerBlock, isSuperCon, 0);
    }

    public WireProperties(int voltage, int baseAmperage, int lossPerBlock, boolean isSuperCon,
                          int criticalTemperature) {
        this.voltage = voltage;
        this.amperage = baseAmperage;
        this.lossPerBlock = isSuperCon ? 0 : lossPerBlock;
        this.superconductorCriticalTemperature = isSuperCon ? criticalTemperature : 0;
        this.isSuperconductor = isSuperCon;
    }

    /**
     * Default values constructor
     */
    public WireProperties() {
        this(8, 1, 1, false);
    }

    /**
     * Retrieves the current wire voltage
     *
     * @return The current wire voltage
     */
    public int getVoltage() {
        return voltage;
    }

    /**
     * Sets the current wire voltage
     *
     * @param voltage The new wire voltage
     */
    public WireProperties setVoltage(int voltage) {
        this.voltage = voltage;
        return this;
    }

    /**
     * Retrieves the current wire amperage
     *
     * @return The current wire amperage
     */
    public int getAmperage() {
        return amperage;
    }

    /**
     * Sets the current wire amperage
     *
     * @param amperage The new current wire amperage
     */
    public WireProperties setAmperage(int amperage) {
        this.amperage = amperage;
        return this;
    }

    /**
     * Retrieves the current wire loss per block
     *
     * @return The current wire loss per block
     */
    public int getLossPerBlock() {
        return lossPerBlock;
    }

    /**
     * Sets the current wire loss per block
     *
     * @param lossPerBlock The new wire loss per block
     */
    public WireProperties setLossPerBlock(int lossPerBlock) {
        this.lossPerBlock = lossPerBlock;
        return this;
    }

    /**
     * If the current wire is a Superconductor wire
     *
     * @return {@code true} if the current wire is a Superconductor
     */
    public boolean isSuperconductor() {
        return isSuperconductor;
    }

    /**
     * Sets the current wire to a superconductor wire
     *
     * @param isSuperconductor The new wire superconductor status
     */
    public WireProperties setSuperconductor(boolean isSuperconductor) {
        this.isSuperconductor = isSuperconductor;
        return this;
    }

    /**
     * Retrieves the critical temperature of the superconductor (the temperature at which the superconductive phase
     * transition happens)
     *
     * @return Critical temperature of the material
     */
    public int getSuperconductorCriticalTemperature() {
        return superconductorCriticalTemperature;
    }

    /**
     * Sets the material's critical temperature
     *
     * @param criticalTemperature The new critical temperature
     */
    public WireProperties setSuperconductorCriticalTemperature(int criticalTemperature) {
        this.superconductorCriticalTemperature = this.isSuperconductor ? criticalTemperature : 0;
        return this;
    }

    @Override
    public void verifyProperty(MaterialProperties properties) {
        properties.ensureSet(PropertyKey.DUST, true);
        if (properties.hasProperty(PropertyKey.INGOT)) {
            // Ensure all Materials with Cables and voltage tier IV or above have a Foil for recipe generation
            Material thisMaterial = properties.getMaterial();
            if (!isSuperconductor && voltage >= GTValues.V[GTValues.IV] && !thisMaterial.hasFlag(GENERATE_FOIL)) {
                thisMaterial.addFlags(GENERATE_FOIL);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WireProperties)) return false;
        WireProperties that = (WireProperties) o;
        return voltage == that.voltage &&
                amperage == that.amperage &&
                lossPerBlock == that.lossPerBlock &&
                superconductorCriticalTemperature == that.superconductorCriticalTemperature &&
                isSuperconductor == that.isSuperconductor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(voltage, amperage, lossPerBlock);
    }
}

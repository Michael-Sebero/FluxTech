package gregtech.api.capability.impl;

import gregtech.api.GTValues;
import gregtech.api.capability.FeCompat;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IEnergyContainer;
import gregtech.common.pipelike.cable.net.EnergyNetHandler;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import org.jetbrains.annotations.NotNull;

public class FEToEUProvider extends CapabilityCompatProvider {

    public FEToEUProvider(ICapabilityProvider upValue) {
        super(upValue);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY &&
                hasUpvalueCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
        if (capability != CapabilityEnergy.ENERGY) {
            return null;
        }

        IEnergyContainer energyContainer = getUpvalueCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER,
                facing);
        return energyContainer == null ? null :
                CapabilityEnergy.ENERGY.cast(new FEEnergyWrapper(energyContainer, facing));
    }

    public class FEEnergyWrapper implements IEnergyStorage {

        private final IEnergyContainer energyContainer;
        private final EnumFacing facing;

        public FEEnergyWrapper(IEnergyContainer energyContainer, EnumFacing facing) {
            this.energyContainer = energyContainer;
            this.facing = facing;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!canReceive()) return 0;

            if (maxReceive == 1 && simulate) {
                return energyContainer.getEnergyCanBeInserted() > 0L ? 1 : 0;
            }

            long maxIn = maxReceive / FeCompat.ratio(true);
            long missing = energyContainer.getEnergyCanBeInserted();
            long voltage = energyContainer.getInputVoltage();
            maxIn = Math.min(missing, maxIn);
            long maxAmp = Math.min(energyContainer.getInputAmperage(), maxIn / voltage);

            if (GTValues.ignoreCableAmperageLimit && energyContainer instanceof EnergyNetHandler) {
                maxIn = maxReceive / FeCompat.ratio(true);
                maxAmp = maxIn / voltage;
            }

            if (maxAmp < 1L) return 0;

            if (!simulate) {
                maxAmp = energyContainer.acceptEnergyFromNetwork(facing, voltage, maxAmp);
            }

            return safeCastLongToInt(maxAmp * voltage * FeCompat.ratio(false));
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return safeCastLongToInt(energyContainer.getEnergyStored() * FeCompat.ratio(false));
        }

        @Override
        public int getMaxEnergyStored() {
            return safeCastLongToInt(energyContainer.getEnergyCapacity() * FeCompat.ratio(false));
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return energyContainer.inputsEnergy(this.facing);
        }

        private int safeCastLongToInt(long v) {
            return v > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) v;
        }
    }
}

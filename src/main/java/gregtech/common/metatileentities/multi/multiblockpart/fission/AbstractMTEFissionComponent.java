package gregtech.common.metatileentities.multi.multiblockpart.fission;

import gregtech.api.GTValues;
import gregtech.api.fission.FissionReactorController;
import gregtech.api.fission.component.FissionComponent;
import gregtech.api.fission.component.FissionComponentData;
import gregtech.api.metatileentity.multiblock.AbilityInstances;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;

import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMTEFissionComponent<T extends FissionComponentData> extends MetaTileEntityMultiblockPart
                                                 implements FissionComponent, IMultiblockAbilityPart<FissionComponent> {

    protected @Nullable T componentData;

    public AbstractMTEFissionComponent(@NotNull ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GTValues.EV);
    }

    protected final boolean isLocked() {
        if (getController() instanceof FissionReactorController controller) {
            return controller.isLocked();
        }
        return false;
    }

    @Override
    public final MultiblockAbility<FissionComponent> getAbility() {
        return MultiblockAbility.FISSION_COMPONENT;
    }

    @Override
    public final void registerAbilities(@NotNull AbilityInstances abilityInstances) {
        if (abilityInstances.isKey(MultiblockAbility.FISSION_COMPONENT)) {
            abilityInstances.add(this);
        }
    }
}

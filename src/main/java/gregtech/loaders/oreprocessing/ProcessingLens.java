package gregtech.loaders.oreprocessing;

import gregtech.api.GregTech_API;
import gregtech.api.GT_Values;
import gregtech.api.unification.ore.IOreRegistrationHandler;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_MultiTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.unification.OreDictionaryUnifier;
import net.minecraft.item.ItemStack;

public class ProcessingLens implements IOreRegistrationHandler {
    public ProcessingLens() {
        OrePrefixes.lens.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        GT_Values.RA.addLatheRecipe(OreDictionaryUnifier.get(OrePrefixes.plate, aMaterial, 1L), OreDictionaryUnifier.get(OrePrefixes.lens, aMaterial, 1L), OreDictionaryUnifier.get(OrePrefixes.dustSmall, aMaterial, 1L), (int) Math.max(aMaterial.getMass() / 2L, 1L), 16);
        GregTech_API.registerCover(aStack, new GT_MultiTexture(Textures.BlockIcons.MACHINE_CASINGS[2][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_LENS, aMaterial.mRGBa)), new gregtech.common.covers.GT_Cover_Lens(aMaterial.mColor.mIndex));
    }
}

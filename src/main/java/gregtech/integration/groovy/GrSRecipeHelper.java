package gregtech.integration.groovy;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.integration.RecipeCompatUtil;

import net.minecraft.item.ItemStack;

import com.cleanroommc.groovyscript.helper.ingredient.GroovyScriptCodeConverter;
import com.cleanroommc.groovyscript.helper.ingredient.NbtHelper;

public class GrSRecipeHelper {

    public static String getRecipeRemoveLine(RecipeMap<?> recipeMap, Recipe recipe) {
        StringBuilder builder = new StringBuilder();
        builder.append("mods.gregtech.")
                .append(recipeMap.unlocalizedName)
                .append(".removeByInput(")
                .append(recipe.getEUt())
                .append(", ");

        if (!recipe.getInputs().isEmpty()) {
            builder.append("[");
            for (GTRecipeInput ci : recipe.getInputs()) {
                String ingredient = getGroovyItemString(ci);
                builder.append(ingredient);
            }
            builder.delete(builder.length() - 2, builder.length())
                    .append("], ");
        } else {
            builder.append("null, ");
        }

        if (!recipe.getFluidInputs().isEmpty()) {
            builder.append("[");
            for (GTRecipeInput fluidIngredient : recipe.getFluidInputs()) {
                builder.append(GroovyScriptCodeConverter.asGroovyCode(fluidIngredient.getInputFluidStack(), false));

                builder.append(", ");
            }
            builder.delete(builder.length() - 2, builder.length())
                    .append("]");
        } else {
            builder.append("null");
        }

        builder.append(")");
        return builder.toString();
    }

    public static String getGroovyItemString(GTRecipeInput recipeInput) {
        StringBuilder builder = new StringBuilder();
        ItemStack itemStack = null;
        String itemId = null;
        for (ItemStack item : recipeInput.getInputStacks()) {
            itemId = RecipeCompatUtil.getMetaItemId(item);
            if (itemId != null) {
                builder.append("metaitem('")
                        .append(itemId)
                        .append("')");
                itemStack = item;
                break;
            } else if (itemStack == null) {
                itemStack = item;
            }
        }
        if (itemStack != null) {
            if (itemId == null) {
                builder.append(GroovyScriptCodeConverter.asGroovyCode(itemStack, false));
            }

            if (itemStack.getTagCompound() != null) {
                builder.append(".withNbt(")
                        .append(NbtHelper.toGroovyCode(itemStack.getTagCompound(), false, false))
                        .append(")");
            }
        }

        if (recipeInput.getAmount() > 1) {
            builder.append(" * ")
                    .append(recipeInput.getAmount());
        }
        builder.append(", ");
        return builder.toString();
    }
}

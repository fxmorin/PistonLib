package ca.fxco.api.pistonlib.recipes;

import ca.fxco.api.pistonlib.containers.CrushingContainer;
import ca.fxco.pistonlib.base.ModRecipeTypes;
import net.minecraft.world.item.crafting.*;

/**
 * A recipe used to combine two items into a single item
 */
public interface PistonCrushingRecipe extends Recipe<PistonCrushingInput> {

    @Override
    default RecipeType<? extends Recipe<PistonCrushingInput>> getType() {
        return ModRecipeTypes.PISTON_CRUSHING;
    }

    int getResultSize();


    @Override
    default boolean isSpecial() {
        return true;
    }

    @Override
    default RecipeBookCategory recipeBookCategory() {
        return null;
    }

}

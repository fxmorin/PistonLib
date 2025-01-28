package ca.fxco.pistonlib.api.recipes;

import net.minecraft.world.item.crafting.*;

/**
 * The piston crafting recipe interface.
 * This is the interface used to define all piston crushing recipes.
 *
 * @author FX
 * @since 1.2.0
 */
public interface PistonCrushingRecipe extends Recipe<PistonCrushingInput> {

    /**
     * The stack size of the returned item from the recipe.
     *
     * @return The stack size of the resulting item from the recipe.
     * @since 1.2.0
     */
    int getResultSize();

    @Override
    default boolean isSpecial() {
        return true;
    }
}

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
     * The number of input ingredients needed for the recipe.
     *
     * @return The number of input ingredients needed
     * @since 1.2.0
     */
    default int getInputCount() {
        return 1;
    }

    @Override
    default boolean isSpecial() {
        return true;
    }
}

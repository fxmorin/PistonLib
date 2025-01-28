package ca.fxco.pistonlib.base;

import ca.fxco.pistonlib.api.recipes.PistonCrushingRecipe;
import ca.fxco.pistonlib.PistonLib;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeTypes {

    public static final RecipeType<? extends PistonCrushingRecipe> PISTON_CRUSHING = register("piston_crushing");

    private static <T extends Recipe<?>> RecipeType<T> register(String name) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, PistonLib.id(name), new RecipeType<T>() {
            public String toString() {
                return name;
            }
        });
    }

    public static void boostrap() { }
}

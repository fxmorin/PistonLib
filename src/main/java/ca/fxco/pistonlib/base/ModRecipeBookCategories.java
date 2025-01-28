package ca.fxco.pistonlib.base;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModRecipeBookCategories {

    public static final RecipeBookCategory PISTON = register("piston");

    private static RecipeBookCategory register(String name) {
        return Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, id(name), new RecipeBookCategory());
    }

    public static void bootstrap() { }
}

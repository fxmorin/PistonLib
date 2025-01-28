package ca.fxco.api.pistonlib.recipes.pistonCrushing;

import ca.fxco.api.pistonlib.recipes.PistonCrushingInput;
import ca.fxco.api.pistonlib.recipes.PistonCrushingRecipe;
import ca.fxco.pistonlib.base.ModRecipeBookCategories;
import ca.fxco.pistonlib.base.ModRecipeTypes;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class AbstractCrushingRecipe implements PistonCrushingRecipe {

    protected final ItemStack result;
    protected PlacementInfo info;

    public AbstractCrushingRecipe(ItemStack result) {
        this.result = result;
    }

    @Override
    public @NotNull ItemStack assemble(PistonCrushingInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public int getResultSize() {
        return result.getCount();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<PistonCrushingInput>> getType() {
        return ModRecipeTypes.PISTON_CRUSHING;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ModRecipeBookCategories.PISTON;
    }
}

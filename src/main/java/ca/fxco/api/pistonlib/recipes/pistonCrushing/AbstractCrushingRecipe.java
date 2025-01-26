package ca.fxco.api.pistonlib.recipes.pistonCrushing;

import ca.fxco.api.pistonlib.recipes.PistonCrushingInput;
import ca.fxco.api.pistonlib.recipes.PistonCrushingRecipe;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;

@Getter
public abstract class AbstractCrushingRecipe implements PistonCrushingRecipe {

    final ItemStack result;
    protected PlacementInfo info;

    public AbstractCrushingRecipe(ItemStack result) {
        this.result = result;
    }

    @Override
    public ItemStack assemble(PistonCrushingInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public int getResultSize() {
        return result.getCount();
    }
}

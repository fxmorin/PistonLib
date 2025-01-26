package ca.fxco.api.pistonlib.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record PistonCrushingInput(List<ItemStack> items, BlockState againstBlock) implements RecipeInput {

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= items.size()) {
            return ItemStack.EMPTY;
        }

        return items.get(slot);
    }

    @Override
    public int size() {
        return items().size();
    }
}

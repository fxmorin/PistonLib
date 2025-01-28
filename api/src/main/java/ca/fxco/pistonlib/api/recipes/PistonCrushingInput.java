package ca.fxco.pistonlib.api.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The recipe input used for the piston crushing recipes.
 * Specifies the items we're trying to find a recipe for,
 * and the block state that the item entity is being crushed against.
 *
 * @author 1Foxy2
 * @since 1.2.0
 */
public class PistonCrushingInput implements RecipeInput {

    private final List<ItemStack> items;
    private final @Nullable BlockState againstBlock;

    /**
     * Constructor for the {@link PistonCrushingInput}
     *
     * @param items        The item stacks which we're trying to find a recipe for
     * @param againstBlock The block state which the item entity is being crushed against,
     *                    if it's being crushed against a block
     * @since 1.2.0
     */
    public PistonCrushingInput(List<ItemStack> items, @Nullable BlockState againstBlock) {
        this.items = items;
        this.againstBlock = againstBlock;
    }

    /**
     * Get the list of item stacks in the input.
     *
     * @return The list of item stacks
     * @since 1.2.0
     */
    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * Get the block state that the item entity is being crushed against
     *
     * @return The {@link BlockState} of the block that the item entity is being crushed against,
     *         or {@code null} if not being crushed against a block.
     * @since 1.2.0
     */
    public @Nullable BlockState getAgainstBlock() {
        return againstBlock;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        if (slot >= items.size()) {
            return ItemStack.EMPTY;
        }
        return items.get(slot);
    }

    @Override
    public int size() {
        return items.size();
    }
}

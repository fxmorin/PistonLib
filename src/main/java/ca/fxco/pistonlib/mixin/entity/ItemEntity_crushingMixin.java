package ca.fxco.pistonlib.mixin.entity;

import ca.fxco.pistonlib.api.recipes.PistonCrushingInput;
import ca.fxco.pistonlib.api.recipes.PistonCrushingRecipe;
import ca.fxco.pistonlib.base.ModRecipeTypes;
import ca.fxco.pistonlib.api.entity.EntityPistonMechanics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;

// Item crushing concept taken from: https://github.com/wisp-forest/things/blob/1.19/src/main/java/com/glisco/things/mixin/EntityMixin.java#L75
@Mixin(ItemEntity.class)
public abstract class ItemEntity_crushingMixin extends Entity implements EntityPistonMechanics {

    @Shadow public abstract ItemStack getItem();

    public ItemEntity_crushingMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean pl$canPushIntoBlocks() {
        return true;
    }

    @Override
    public void pl$onPistonCrushing(@Nullable BlockState crushedAgainst) {
        if (this.isRemoved()) {
            return;
        }

        Level level = this.level();
        List<ItemEntity> itemEntities = level.getEntities(
                EntityTypeTest.forClass(ItemEntity.class), new AABB(this.blockPosition()), ItemEntity::isAlive);

        if (itemEntities.isEmpty()) {
            return;
        }

        List<ItemStack> itemsToMerge = new ArrayList<>();

        for (ItemEntity itemEntity : itemEntities) {
            itemsToMerge.add(itemEntity.getItem());
        }

        PistonCrushingInput input = new PistonCrushingInput(itemsToMerge, crushedAgainst);

        Optional<? extends RecipeHolder<? extends PistonCrushingRecipe>> optionalRecipe = level.getServer().getRecipeManager()
                .getRecipeFor(ModRecipeTypes.PISTON_CRUSHING, input, level);
        if (optionalRecipe.isEmpty()) {
            return;
        }
        PistonCrushingRecipe crushingRecipe = optionalRecipe.get().value();
        int ingredientAmount = 0;
        for (ItemStack itemStack : input.getItems()) {
            if (ingredientAmount == 0) {
                ingredientAmount = itemStack.getCount();
            } else {
                ingredientAmount = Math.min(itemStack.getCount(), ingredientAmount);
            }
        }

        // Round down to the input count
        int inputCount = crushingRecipe.getInputCount();
        ingredientAmount -= (ingredientAmount % inputCount);

        for (ItemStack itemStack : input.getItems()) {
            itemStack.shrink(ingredientAmount);
        }

        int amount = ingredientAmount / inputCount; // How many times to do the crafting recipe

        var registryAccess = level.registryAccess();
        while (amount > 0) {
            amount--;
            ItemStack resultItem = crushingRecipe.assemble(input, registryAccess);
            while (resultItem.getCount() > resultItem.getMaxStackSize()) { // Split into stacks of max size
                level.addFreshEntity(new ItemEntity(
                        level,
                        this.getX(), this.getY(), this.getZ(),
                        resultItem.split(resultItem.getMaxStackSize())
                ));
            }
            level.addFreshEntity(new ItemEntity(
                    level,
                    this.getX(), this.getY(), this.getZ(),
                    resultItem
            ));
        }
    }
}

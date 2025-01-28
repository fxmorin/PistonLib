package ca.fxco.pistonlib.mixin.entity;

import ca.fxco.api.pistonlib.recipes.PistonCrushingInput;
import ca.fxco.api.pistonlib.recipes.PistonCrushingRecipe;
import ca.fxco.pistonlib.base.ModRecipeTypes;
import ca.fxco.api.pistonlib.entity.EntityPistonMechanics;
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

        List<ItemEntity> itemEntities = this.level().getEntities(
                EntityTypeTest.forClass(ItemEntity.class), new AABB(this.blockPosition()), ItemEntity::isAlive);

        if (itemEntities.isEmpty()) {
            return;
        }

        List<ItemStack> itemsToMerge = new ArrayList<>();

        for (ItemEntity itemEntity : itemEntities) {
            itemsToMerge.add(itemEntity.getItem());
        }

        PistonCrushingInput input = new PistonCrushingInput(itemsToMerge, crushedAgainst);

        Optional<? extends RecipeHolder<? extends PistonCrushingRecipe>> optionalRecipe = this.level().getServer().getRecipeManager()
                .getRecipeFor(ModRecipeTypes.PISTON_CRUSHING, input, this.level());
        if (optionalRecipe.isEmpty()) {
            return;
        }
        PistonCrushingRecipe crushingRecipe = optionalRecipe.get().value();
        List<ItemStack> results = new ArrayList<>();
        int ingredientAmount = 0;
        for (ItemStack itemStack : input.items()) {
            if (ingredientAmount == 0) {
                ingredientAmount = itemStack.getCount();
            } else {
                ingredientAmount = Math.min(itemStack.getCount(), ingredientAmount);
            }
        }
        for (ItemStack itemStack : input.items()) {
            itemStack.shrink(ingredientAmount);
        }

        ingredientAmount = ingredientAmount * crushingRecipe.getResultSize();
        ItemStack resultItem;
        while (true) {
            resultItem = crushingRecipe.assemble(input, level().registryAccess());
            if (ingredientAmount > resultItem.getMaxStackSize()) {
                ingredientAmount -= resultItem.getMaxStackSize();
                resultItem.setCount(resultItem.getMaxStackSize());
            } else {
                resultItem.setCount(ingredientAmount);

                this.level().addFreshEntity(
                        new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), resultItem));
                break;
            }
            this.level().addFreshEntity(
                    new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), resultItem));
        }
    }
}

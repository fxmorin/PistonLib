package ca.fxco.api.pistonlib.recipes.pistonCrushing.builders;

import ca.fxco.api.pistonlib.recipes.pistonCrushing.SingleCrushingAgainstRecipe;
import ca.fxco.api.pistonlib.recipes.pistonCrushing.SingleCrushingConditionalRecipe;
import ca.fxco.api.pistonlib.recipes.pistonCrushing.SingleCrushingRecipe;
import com.mojang.datafixers.util.Either;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class SingleCrushingRecipeBuilder implements RecipeBuilder {
    protected final ItemStack result;
    protected final Ingredient ingredient;
    @Nullable
    protected String group;
    @Nullable
    protected Block againstBlock;
    @Nullable
    protected SingleCrushingConditionalRecipe.Condition condition;
    @Nullable
    protected Either<Float, Either<Block, String>> data;

    public SingleCrushingRecipeBuilder(Ingredient ingredient, ItemStack itemStack, int count) {
        this.ingredient = ingredient;
        this.result = itemStack;
        result.setCount(count);
    }

    public static SingleCrushingRecipeBuilder crushing(Ingredient ingredient, ItemStack itemStack) {
        return crushing(ingredient, itemStack, 1);
    }

    public static SingleCrushingRecipeBuilder crushing(Ingredient ingredient, ItemStack itemStack, int count) {
        return new SingleCrushingRecipeBuilder(ingredient, itemStack, count);
    }

    public SingleCrushingRecipeBuilder mustBeAgainst(Block againstBlock) {
        if (this.condition != null) {
            throw new IllegalStateException("You can only use either `mustBeAgainst` or `hasConditional`");
        }
        this.againstBlock = againstBlock;
        return this;
    }

    public SingleCrushingRecipeBuilder hasConditional(SingleCrushingConditionalRecipe.Condition condition, Object data) {
        if (this.againstBlock != null) {
            throw new IllegalStateException("You can only use either `mustBeAgainst` or `hasConditional`");
        }
        this.condition = condition;
        if (data instanceof Block block) {
            this.data = Either.right(Either.left(block));
        } else if (data instanceof Float floatNum) {
            this.data = Either.left(floatNum);
        } else if (data instanceof String string) {
            this.data = Either.right(Either.right(string));
        }
        return this;
    }

    @Override
    public SingleCrushingRecipeBuilder unlockedBy(String string, Criterion<?> criterion) {
        return this;
    }

    public SingleCrushingRecipeBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        if (this.againstBlock != null) {
            output.accept(key, new SingleCrushingAgainstRecipe(this.ingredient, this.result, this.againstBlock), null);
        } else if (this.condition != null) {
            output.accept(key, new SingleCrushingConditionalRecipe(this.ingredient, this.result, this.condition, this.data), null);
        } else {
            output.accept(key, new SingleCrushingRecipe(this.ingredient, this.result), null);
        }
    }
}

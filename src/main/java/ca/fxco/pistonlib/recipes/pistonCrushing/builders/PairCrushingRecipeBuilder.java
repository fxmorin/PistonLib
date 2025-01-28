package ca.fxco.pistonlib.recipes.pistonCrushing.builders;

import ca.fxco.pistonlib.recipes.pistonCrushing.PairCrushingRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public class PairCrushingRecipeBuilder implements RecipeBuilder {

    protected final Ingredient first;
    protected final Ingredient second;
    protected final ItemStack result;
    @Nullable
    protected String group;

    public PairCrushingRecipeBuilder(Ingredient first, Ingredient second, ItemStack result) {
        this.first = first;
        this.second = second;
        this.result = result;
    }

    public static PairCrushingRecipeBuilder crushing(Ingredient first, Ingredient second, ItemStack result) {
        return new PairCrushingRecipeBuilder(first, second, result);
    }

    @Override
    public PairCrushingRecipeBuilder unlockedBy(String string, Criterion<?> criterion) {
        return this;
    }

    public PairCrushingRecipeBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        PairCrushingRecipe recipe = new PairCrushingRecipe(this.first, this.second, this.result);
        output.accept(key, recipe, null);
    }
}

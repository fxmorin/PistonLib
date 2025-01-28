package ca.fxco.pistonlib.recipes.pistonCrushing;

import ca.fxco.pistonlib.api.recipes.PistonCrushingInput;
import ca.fxco.pistonlib.base.ModRecipeSerializers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class MultiCrushingRecipe extends AbstractCrushingRecipe {

    @Getter
    final List<Ingredient> ingredients;

    public MultiCrushingRecipe(List<Ingredient> ingredients, ItemStack result) {
        super(result);
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(PistonCrushingInput input, Level level) {
        StackedItemContents stackedContents = new StackedItemContents();
        int i = 0;

        for(int j = 0; j < input.size(); ++j) {
            ItemStack itemStack = input.getItem(j);
            if (!itemStack.isEmpty()) {
                ++i;
                stackedContents.accountStack(itemStack, 1);
            }
        }

        return i == this.ingredients.size() && stackedContents.canCraft(this, null);
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.info == null) {
            this.info = PlacementInfo.create(ingredients);
        }

        return this.info;
    }

    @Override
    public RecipeSerializer<? extends Recipe<PistonCrushingInput>> getSerializer() {
        return ModRecipeSerializers.MULTI_PISTON_CRUSHING;
    }

    public static class Serializer implements RecipeSerializer<MultiCrushingRecipe> {
        public static final MapCodec<MultiCrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(MultiCrushingRecipe::getIngredients),
                ItemStack.CODEC.fieldOf("result").forGetter(MultiCrushingRecipe::getResult)
        ).apply(inst, MultiCrushingRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, MultiCrushingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                        MultiCrushingRecipe::getIngredients,
                        ItemStack.STREAM_CODEC,
                        MultiCrushingRecipe::getResult,
                        MultiCrushingRecipe::new
                );

        @Override
        public MapCodec<MultiCrushingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MultiCrushingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

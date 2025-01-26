package ca.fxco.api.pistonlib.recipes.pistonCrushing;

import ca.fxco.api.pistonlib.recipes.PistonCrushingInput;
import ca.fxco.pistonlib.base.ModRecipeSerializers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

@Getter
public class SingleCrushingRecipe extends AbstractCrushingRecipe {

    protected final Ingredient ingredient;

    public SingleCrushingRecipe(Ingredient ingredient, ItemStack result) {
        super(result);
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(PistonCrushingInput input, Level level) {
        if (input.size() != 1) {
            return false;
        }

        return this.ingredient.test(input.getItem(0));
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.info == null) {
            this.info = PlacementInfo.create(ingredient);
        }

        return this.info;
    }

    @Override
    public RecipeSerializer<? extends Recipe<PistonCrushingInput>> getSerializer() {
        return ModRecipeSerializers.SINGLE_PISTON_CRUSHING;
    }

    public static class Serializer implements RecipeSerializer<SingleCrushingRecipe> {
        public static final MapCodec<SingleCrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleCrushingRecipe::getIngredient),
                ItemStack.CODEC.fieldOf("result").forGetter(SingleCrushingRecipe::getResult)
        ).apply(inst, SingleCrushingRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, SingleCrushingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, SingleCrushingRecipe::getIngredient,
                        ItemStack.STREAM_CODEC, SingleCrushingRecipe::getResult,
                        SingleCrushingRecipe::new
                );

        @Override
        public MapCodec<SingleCrushingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SingleCrushingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

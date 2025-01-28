package ca.fxco.pistonlib.recipes.pistonCrushing;

import ca.fxco.pistonlib.api.recipes.PistonCrushingInput;
import ca.fxco.pistonlib.base.ModRecipeSerializers;
import ca.fxco.pistonlib.network.PLServerNetwork;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * You also specify what block you must be crushed against for this to work
 */
@Getter
public class SingleCrushingAgainstRecipe extends SingleCrushingRecipe {

    protected final @NotNull Block againstBlock;

    public SingleCrushingAgainstRecipe(Ingredient ingredient, int ingredientAmount, ItemStack result,
                                       @NotNull Block againstBlock) {
        super(ingredient, ingredientAmount, result);
        this.againstBlock = againstBlock;
    }

    @Override
    public boolean matches(PistonCrushingInput input, Level level) {
        BlockState state = input.getAgainstBlock();
        return state != null && state.getBlock() == againstBlock && super.matches(input, level);
    }

    @Override
    public RecipeSerializer<? extends Recipe<PistonCrushingInput>> getSerializer() {
        return ModRecipeSerializers.SINGLE_AGAINST_PISTON_CRUSHING;
    }

    public static class Serializer implements RecipeSerializer<SingleCrushingAgainstRecipe> {
        public static final Codec<Block> BLOCK_CODEC = BuiltInRegistries.BLOCK.byNameCodec();

        public static final MapCodec<SingleCrushingAgainstRecipe> CODEC = RecordCodecBuilder.mapCodec(
                inst -> inst.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleCrushingAgainstRecipe::getIngredient),
                        Codec.INT.optionalFieldOf("ingredientAmount", 1).forGetter(SingleCrushingRecipe::getIngredientAmount),
                        ItemStack.CODEC.fieldOf("result").forGetter(SingleCrushingAgainstRecipe::getResult),
                        BLOCK_CODEC.fieldOf("against_block").forGetter(SingleCrushingAgainstRecipe::getAgainstBlock)
                ).apply(inst, SingleCrushingAgainstRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, SingleCrushingAgainstRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC,
                        SingleCrushingAgainstRecipe::getIngredient,
                        ByteBufCodecs.INT, SingleCrushingRecipe::getIngredientAmount,
                        ItemStack.STREAM_CODEC,
                        SingleCrushingAgainstRecipe::getResult,
                        PLServerNetwork.BLOCK_STREAM_CODEC,
                        SingleCrushingAgainstRecipe::getAgainstBlock,
                        SingleCrushingAgainstRecipe::new
                );

        @Override
        public MapCodec<SingleCrushingAgainstRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SingleCrushingAgainstRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

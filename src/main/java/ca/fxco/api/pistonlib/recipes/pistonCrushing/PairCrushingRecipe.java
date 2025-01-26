package ca.fxco.api.pistonlib.recipes.pistonCrushing;

import ca.fxco.api.pistonlib.recipes.PistonCrushingInput;
import ca.fxco.pistonlib.base.ModRecipeSerializers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * A piston crushing recipe that simply merges two different items together.
 * Container is only 2 slots
 */
public class PairCrushingRecipe extends AbstractCrushingRecipe {

    @Getter
    final Ingredient first;
    @Getter
    final Ingredient second;

    public PairCrushingRecipe(Ingredient first, Ingredient second, ItemStack result) {
        super(result);
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean matches(PistonCrushingInput input, Level level) {
        if (input.size() != 2) {
            return false;
        }
        ItemStack first = input.getItem(0);
        ItemStack second = input.getItem(1);

        return (this.first.test(first) && this.second.test(second)) ||
                (this.first.test(second) && this.second.test(first));
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.info == null) {
            this.info = PlacementInfo.create(List.of(first, second));
        }

        return this.info;
    }

    @Override
    public RecipeSerializer<? extends Recipe<PistonCrushingInput>> getSerializer() {
        return ModRecipeSerializers.PAIR_PISTON_CRUSHING;
    }

    public static class Serializer implements RecipeSerializer<PairCrushingRecipe> {
        public static final MapCodec<PairCrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("first").forGetter(PairCrushingRecipe::getFirst),
                Ingredient.CODEC.fieldOf("second").forGetter(PairCrushingRecipe::getSecond),
                ItemStack.CODEC.fieldOf("result").forGetter(PairCrushingRecipe::getResult)
        ).apply(inst, PairCrushingRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, PairCrushingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, PairCrushingRecipe::getFirst,
                        Ingredient.CONTENTS_STREAM_CODEC, PairCrushingRecipe::getSecond,
                        ItemStack.STREAM_CODEC, PairCrushingRecipe::getResult,
                        PairCrushingRecipe::new
                );

        @Override
        public MapCodec<PairCrushingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PairCrushingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

package ca.fxco.api.pistonlib.recipes.pistonCrushing;

import ca.fxco.api.pistonlib.recipes.PistonCrushingInput;
import ca.fxco.pistonlib.base.ModRecipeSerializers;
import ca.fxco.pistonlib.network.PLServerNetwork;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.IntFunction;

@Getter
public class SingleCrushingConditionalRecipe extends SingleCrushingRecipe {

    protected final Condition condition;
    protected final Either<Float, Either<Block, String>> data;

    public SingleCrushingConditionalRecipe(Ingredient ingredient, ItemStack result, Condition condition, Either<Float, Either<Block, String>> data) {
        super(ingredient, result);
        this.condition = condition;
        this.data = data;
    }

    @Override
    public boolean matches(PistonCrushingInput input, Level level) {
        if (input.againstBlock() == null) {
            return false;
        }

        return matchesCondition(condition, data, input.againstBlock().getBlock()) &&
                super.matches(input, level);
    }

    @Override
    public RecipeSerializer<? extends Recipe<PistonCrushingInput>> getSerializer() {
        return ModRecipeSerializers.SINGLE_CONDITIONAL_PISTON_CRUSHING;
    }

    private static boolean matchesCondition(Condition condition, Either<Float, Either<Block, String>> data, Block block) {
        if (block == null) {
            return false;
        }
        return switch (condition) {
            case EQUALS -> data.right().orElse(Either.left(Blocks.AIR)).left().orElse(Blocks.AIR).getClass() == block.getClass();
            case INSTANCEOF -> data.right().orElse(Either.left(Blocks.AIR)).left().orElse(Blocks.AIR).getClass().isInstance(block);
            case NAME_CONTAINS -> block.getDescriptionId().contains(data.right().orElse(Either.right("")).right().orElse(""));
            case NAME_REGEX -> block.getDescriptionId().matches(data.right().orElse(Either.right("")).right().orElse(""));
            case HIGHER_DESTROY_TIME -> block.defaultDestroyTime() > data.left().orElse(0f);
            case LOWER_DESTROY_TIME -> block.defaultDestroyTime() < data.left().orElse(0f);
            case HIGHER_RESISTANCE -> block.getExplosionResistance() > data.left().orElse(0f);
            case LOWER_RESISTANCE -> block.getExplosionResistance() < data.left().orElse(0f);
        };
    }

    public static class Serializer implements RecipeSerializer<SingleCrushingConditionalRecipe> {
        public static final MapCodec<SingleCrushingConditionalRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleCrushingConditionalRecipe::getIngredient),
                ItemStack.CODEC.fieldOf("result").forGetter(SingleCrushingConditionalRecipe::getResult),
                Condition.CODEC.fieldOf("condition").forGetter(SingleCrushingConditionalRecipe::getCondition),
                Codec.either(Codec.FLOAT, Codec.either(SingleCrushingAgainstRecipe.Serializer.BLOCK_CODEC, Codec.STRING)).fieldOf("data")
                        .forGetter(SingleCrushingConditionalRecipe::getData)
        ).apply(inst, SingleCrushingConditionalRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, SingleCrushingConditionalRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC,
                        SingleCrushingConditionalRecipe::getIngredient,
                        ItemStack.STREAM_CODEC,
                        SingleCrushingConditionalRecipe::getResult,
                        ByteBufCodecs.idMapper(Condition.BY_ID, Condition::getId),
                        SingleCrushingConditionalRecipe::getCondition,
                        ByteBufCodecs.either(ByteBufCodecs.FLOAT,
                                ByteBufCodecs.either(PLServerNetwork.BLOCK_STREAM_CODEC, ByteBufCodecs.STRING_UTF8)),
                        SingleCrushingConditionalRecipe::getData,
                        SingleCrushingConditionalRecipe::new
                );

        @Override
        public MapCodec<SingleCrushingConditionalRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SingleCrushingConditionalRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    @Getter
    public enum Condition implements StringRepresentable {
        EQUALS(0),
        INSTANCEOF(1),
        NAME_CONTAINS(2),
        NAME_REGEX(3),
        HIGHER_DESTROY_TIME(4),
        LOWER_DESTROY_TIME(5),
        HIGHER_RESISTANCE(6),
        LOWER_RESISTANCE(7);

        private static final Codec<Condition> CODEC = StringRepresentable.fromEnum(Condition::values);

        private final int id;

        Condition(int id) {
            this.id = id;
        }

        public static final IntFunction<Condition> BY_ID =
                ByIdMap.continuous(
                        Condition::getId,
                        Condition.values(),
                        ByIdMap.OutOfBoundsStrategy.ZERO
                );

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}

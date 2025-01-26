package ca.fxco.pistonlib.api.pistonLogic.families;

import java.util.Map;
import java.util.function.BiFunction;

import ca.fxco.pistonlib.api.pistonLogic.structure.StructureGroup;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;

/**
 * A piston family is a collection of blocks and a block entity type
 * that form a set a pistons that act according to the piston behavior
 * as defined in the family.
 * <p>
 * A piston family holds references to piston base blocks for each
 * {@link net.minecraft.world.level.block.state.properties.PistonType
 * PistonType}, a piston head block, a moving block, a moving block
 * entity type, and a moving block entity factory. If the max length
 * of the pistons as defined in the piston behavior is more than 1,
 * the family must also hold a reference to a piston arm block.
 * 
 * @author FX
 * @since 1.2.0
 */
//TODO-API: JavaDoc
public interface PistonFamily {

    @Nullable Block getArm();

    Block getHead();
    Block getMoving();
    BlockEntityType<? extends PistonMovingBlockEntity> getMovingBlockEntityType();
    Factory<? extends PistonMovingBlockEntity> getMovingBlockEntityFactory();
    /**
     * @return an arbitrary base block
     */
    Block getBase();
    @Nullable Block getBase(PistonType type);
    Map<PistonType, Block> getBases();

    boolean hasCustomTextures();

    default boolean hasCustomLength() {
        return getMinLength() != 0 || getMaxLength() != 1;
    }

    PistonMovingBlockEntity newMovingBlockEntity(BlockPos pos, BlockState state, BlockState movedState,
                                                 BlockEntity movedBlockEntity, Direction facing, boolean extending,
                                                 boolean isSourcePiston);

    PistonMovingBlockEntity newMovingBlockEntity(StructureGroup structureGroup, BlockPos pos, BlockState state,
                                                 BlockState movedState, BlockEntity movedBlockEntity, Direction facing,
                                                 boolean extending, boolean isSourcePiston);

    //region PistonBehavior Delegate

    boolean isVerySticky();

    boolean isFrontPowered();

    boolean isSlippery();

    boolean isQuasi();

    int getPushLimit();

    float getExtendingSpeed();

    float getRetractingSpeed();

    boolean isRetractOnExtending();

    boolean isExtendOnRetracting();

    int getMinLength();

    int getMaxLength();

    //endregion

    interface Builder {

        Builder head(Block head);

        Builder arm(@Nullable Block arm);

        Builder moving(Block movingBlock);

        Builder base(PistonType type, Block base);

        Builder behavior(PistonBehavior behavior);

        Builder behavior(PistonBehavior.Builder builder);

        <T extends PistonMovingBlockEntity> Builder movingBlockEntity(BlockEntityType<T> type, Factory<T> factory);

        <T extends PistonMovingBlockEntity> Builder vanillaMovingBlockEntity(
                BlockEntityType<T> type,
                BiFunction<BlockPos, BlockState, T> factory
        );

        Builder customTextures(boolean customTextures);

        PistonFamily build();
    }

    /**
     * Factory used to create PistonMovingBlockEntities
     *
     * @param <T> The type of the piston moving block entity
     */
    @FunctionalInterface
    interface Factory<T extends PistonMovingBlockEntity> {

        T create(PistonFamily family, StructureGroup structureGroup, BlockPos pos, BlockState state,
                 BlockState movedState, BlockEntity movedBlockEntity, Direction facing, boolean extending,
                 boolean isSourcePiston);
    }
}

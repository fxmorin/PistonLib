package ca.fxco.pistonlib.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This interface is used to prepare and get block entity for placement
 *
 * @author Space Walker
 * @since 1.0.4
 */
public interface LevelMBE {

    /**
     * Forcefully set the block entity for the next block placed.
     * This is used to get around some of minecraft's limitations.
     *
     * @param pos block position of the block entity
     * @param state block state of the block entity
     * @param blockEntity block entity to prepare
     * @since 1.0.4
     */
    void pl$prepareBlockEntityPlacement(BlockPos pos, BlockState state, BlockEntity blockEntity);

    /**
     * Gets the forced block entity for the block placement.
     *
     * @param pos block position of the block entity
     * @param state block state of the block entity
     * @return block entity for placement
     * @since 1.0.4
     */
    BlockEntity pl$getBlockEntityForPlacement(BlockPos pos, BlockState state);

}

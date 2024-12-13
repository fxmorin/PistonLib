package ca.fxco.api.pistonlib.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * this interface is used to prepare and get block entity for placement
 * @author Space Walker
 * @since 1.0.4
 */
public interface LevelMBE {

    /**
     * @param pos block pos of the block entity
     * @param state block state of the block entity
     * @param blockEntity block entity to prepare
     */
    void pl$prepareBlockEntityPlacement(BlockPos pos, BlockState state, BlockEntity blockEntity);

    /**
     * @param pos block pos of the block entity
     * @param state block state of the block entity
     * @return block entity for placement
     */
    BlockEntity pl$getBlockEntityForPlacement(BlockPos pos, BlockState state);

}

package ca.fxco.api.pistonlib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Adds the ability to change your power level based on if its quasi powering
 *
 * @author FX
 * @since 1.0.4
 */
public interface BlockQuasiPower {

    /**
     * @param state block state of the block
     * @param level of the block
     * @param pos block position of the block's neighbor (where the check happens)
     * @param dir direction of the neighbor from the block
     * @param dist distance to check above block
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getQuasiSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, int dist);

    /**
     * @param state block state of the block
     * @param level of the block
     * @param pos block position of the block's neighbor (where the check happens)
     * @param dir direction of the neighbor from the block
     * @param dist distance to check above block
     * @return the power of direct quasi signal
     * @since 1.0.4
     */
    int pl$getDirectQuasiSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, int dist);

    /**
     * @param state block state of the block
     * @param level of the block
     * @param pos block position of the block's neighbor (where the check happens)
     * @return true if block's neighbour is quasi conductor
     * @since 1.0.4
     */
    boolean pl$isQuasiConductor(BlockState state, BlockGetter level, BlockPos pos);

}

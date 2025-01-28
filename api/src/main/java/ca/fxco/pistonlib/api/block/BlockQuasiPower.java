package ca.fxco.pistonlib.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Adds the ability to change your power level based on if its quasi powering.
 * This allows for more configurable and conditional quasi powering.
 *
 * @author FX
 * @since 1.0.4
 */
public interface BlockQuasiPower {

    /**
     * Get the quasi signal strength from a distance.
     *
     * @param state block state of the block
     * @param level of the block
     * @param pos   block position of the block where the check happens
     * @param dir   direction of the neighbor from the block
     * @param dist  distance to check above the block
     * @return the power of quasi signal
     * @since 1.0.4
     */
    default int pl$getQuasiSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, int dist) {
        return state.getSignal(level, pos, dir);
    }

    /**
     * Get the direct quasi signal strength from a distance.
     *
     * @param state block state of the block
     * @param level of the block
     * @param pos block position of the block's neighbor (where the check happens)
     * @param dir direction of the neighbor from the block
     * @param dist distance to check above the block
     * @return the power of direct quasi signal
     * @since 1.0.4
     */
    default int pl$getDirectQuasiSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir, int dist) {
        return state.getDirectSignal(level, pos, dir);
    }

    /**
     * Checks if this block is a quasi conductor.
     *
     * @param state block state of the block
     * @param level of the block
     * @param pos block position of the block where the check happens
     * @return {@code true} if block's neighbor is quasi conductor, otherwise {@code false}
     * @since 1.0.4
     */
    default boolean pl$isQuasiConductor(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isRedstoneConductor(level, pos);
    }

}

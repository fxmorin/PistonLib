package ca.fxco.pistonlib.api.block.state;

import ca.fxco.pistonlib.api.block.BlockQuasiPower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

/**
 * This interface is for internal use only.
 * Use {@link BlockQuasiPower} for single block conditions
 * Adds the ability to change your power level based on if its quasi powering.
 *
 * @author FX
 * @since 1.0.4
 */
public interface BlockStateQuasiPower {

    /**
     * Get the quasi signal strength from a distance.
     *
     * @param level of the block
     * @param pos   block position of the block where the check happens
     * @param dir   direction of the neighbor from the block
     * @param dist  distance to check above the block
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getQuasiSignal(BlockGetter level, BlockPos pos, Direction dir, int dist);

    /**
     * Get the direct quasi signal strength from a distance.
     *
     * @param level of the block
     * @param pos block position of the block where the check happens
     * @param dir direction of the neighbor from the block
     * @param dist distance to check above the block
     * @return the power of direct quasi signal
     * @since 1.0.4
     */
    int pl$getDirectQuasiSignal(BlockGetter level, BlockPos pos, Direction dir, int dist);

    /**
     * Checks if this state is a quasi conductor.
     *
     * @param level of the block
     * @param pos block position of the block where the check happens
     * @return {@code true} if block's neighbor is quasi conductor, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$isQuasiConductor(BlockGetter level, BlockPos pos);

}

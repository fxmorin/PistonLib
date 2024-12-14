package ca.fxco.api.pistonlib.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

/**
 * Adds the ability to change your power level based on if its quasi powering
 *
 * @author FX
 * @since 1.0.4
 */
public interface BlockStateQuasiPower {

    /**
     * @param level of the block
     * @param pos block position of the block where the check happens
     * @param dir direction of the neighbor from the block
     * @param dist distance to check above block
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getQuasiSignal(BlockGetter level, BlockPos pos, Direction dir, int dist);

    /**
     * @param level of the block
     * @param pos block position of the block where the check happens
     * @param dir direction of the neighbor from the block
     * @param dist distance to check above block
     * @return the power of direct quasi signal
     * @since 1.0.4
     */
    int pl$getDirectQuasiSignal(BlockGetter level, BlockPos pos, Direction dir, int dist);

    /**
     * @param level of the block
     * @param pos block position of the block where the check happens
     * @return true if block's neighbour is quasi conductor
     * @since 1.0.4
     */
    boolean pl$isQuasiConductor(BlockGetter level, BlockPos pos);

}

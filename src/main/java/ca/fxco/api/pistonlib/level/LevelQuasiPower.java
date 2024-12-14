package ca.fxco.api.pistonlib.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * Adds the ability to change your power level based on if its quasi powering
 *
 * @author FX
 * @since 1.0.4
 */
public interface LevelQuasiPower {

    /**
     * @param pos block position where the check happens
     * @param dist distance to check
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getDirectQuasiSignalTo(BlockPos pos, int dist);

    /**
     * @param pos block position where the check happens
     * @param dist distance to check
     * @return true if block's neighbours has direct quasi signal
     * @since 1.0.4
     */
    boolean pl$hasDirectQuasiSignalTo(BlockPos pos, int dist);

    /**
     * @param pos block position of the block doing the check
     * @param dist distance to check
     * @return the highest power of quasi signal of block's neighbours
     * @since 1.0.4
     */
    int pl$getStrongestQuasiNeighborSignal(BlockPos pos, int dist);

    /**
     * @param pos block position of the block doing the check
     * @param dir direction to check
     * @param dist distance to check
     * @return the highest power of quasi signal of block's neighbours
     * @since 1.0.4
     */
    int pl$getStrongestQuasiNeighborSignal(BlockPos pos, Direction dir, int dist);

    /**
     * @param pos block position of the block doing the check
     * @param dist distance to check
     * @return true if block's neighbours has quasi signal
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignal(BlockPos pos, int dist);

    /**
     * @param pos block position of the block doing the check
     * @param dir direction to check
     * @param dist distance to check
     * @return true if block's neighbours has quasi signal
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignal(BlockPos pos, Direction dir, int dist);

    /**
     * @param pos block position where the check happens
     * @param dir direction to check
     * @param dist distance to check
     * @return true if block has quasi signal
     * @since 1.0.4
     */
    boolean pl$hasQuasiSignal(BlockPos pos, Direction dir, int dist);

    /**
     * @param pos block position where the check happens
     * @param dir direction to check
     * @param dist distance to check
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getQuasiSignal(BlockPos pos, Direction dir, int dist);

    /**
     * @param pos block position where the check happens
     * @param dir direction to check
     * @param dist distance to check
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getDirectQuasiSignal(BlockPos pos, Direction dir, int dist);

    /**
     * @param pos block position of the block doing the check
     * @param dist distance to check
     * @return true if all blocks at the given dist above block has quasi signal
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignalColumn(BlockPos pos, int dist);

    /**
     * @param pos block position of the block doing the check
     * @param dir direction to check
     * @param dist distance to check
     * @return true if all blocks at the given dist above block has quasi signal
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignalColumn(BlockPos pos, Direction dir, int dist);

    /**
     * @param pos block position of the block doing the check
     * @return true if all blocks at the given dist around block has quasi signal
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignalBubble(BlockPos pos);

}

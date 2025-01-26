package ca.fxco.pistonlib.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * Adds the ability to change your power level based on if its quasi powering.
 *
 * @author FX
 * @since 1.0.4
 */
public interface LevelQuasiPower {

    /**
     * Gets the direct quasi signal at a position.
     *
     * @param pos  block position where the check happens
     * @param dist distance to check
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getDirectQuasiSignalTo(BlockPos pos, int dist);

    /**
     * Checks if there's a direct signal at a position.
     *
     * @param pos  block position where the check happens
     * @param dist distance to check
     * @return {@code true} if block's neighbours has direct quasi signal, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$hasDirectQuasiSignalTo(BlockPos pos, int dist);

    /**
     * Finds the strongest quasi neighbor signal at a position.
     *
     * @param pos  block position of the block doing the check
     * @param dist distance to check
     * @return the highest power of quasi signal of block's neighbours
     * @since 1.0.4
     */
    int pl$getStrongestQuasiNeighborSignal(BlockPos pos, int dist);

    /**
     * Finds the strongest quasi neighbor signal at a position, for a side.
     *
     * @param pos  block position of the block doing the check
     * @param dir  direction to check
     * @param dist distance to check
     * @return the highest power of quasi signal of block's neighbours
     * @since 1.0.4
     */
    int pl$getStrongestQuasiNeighborSignal(BlockPos pos, Direction dir, int dist);

    /**
     * Checks if a quasi neighbor signal is present at a position.
     *
     * @param pos  block position of the block doing the check
     * @param dist distance to check
     * @return {@code true} if block's neighbours has quasi signal, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignal(BlockPos pos, int dist);

    /**
     * Checks if a quasi neighbor signal is present at a position, from a side.
     *
     * @param pos  block position of the block doing the check
     * @param dir  direction to check
     * @param dist distance to check
     * @return {@code true} if block's neighbours has quasi signal, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignal(BlockPos pos, Direction dir, int dist);

    /**
     * Checks if a quasi signal is present at a position.
     *
     * @param pos  block position where the check happens
     * @param dir  direction to check
     * @param dist distance to check
     * @return {@code true} if block has quasi signal, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$hasQuasiSignal(BlockPos pos, Direction dir, int dist);

    /**
     * Get the quasi signal at a position.
     *
     * @param pos  block position where the check happens
     * @param dir  direction to check
     * @param dist distance to check
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getQuasiSignal(BlockPos pos, Direction dir, int dist);

    /**
     * Get the direct quasi signal at a position.
     *
     * @param pos  block position where the check happens
     * @param dir  direction to check
     * @param dist distance to check
     * @return the power of quasi signal
     * @since 1.0.4
     */
    int pl$getDirectQuasiSignal(BlockPos pos, Direction dir, int dist);

    /**
     * Checks if all the blocks above a block have a quasi neighbor signal.
     *
     * @param pos  block position of the block doing the check
     * @param dist distance to check
     * @return {@code true} if all blocks at the given dist above block has quasi signal, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignalColumn(BlockPos pos, int dist);

    /**
     * Checks if all the blocks above a block have a quasi neighbor signal.
     *
     * @param pos  block position of the block doing the check
     * @param dir  direction to check
     * @param dist distance to check
     * @return {@code true} if all blocks at the given dist above block has quasi signal, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignalColumn(BlockPos pos, Direction dir, int dist);

    /**
     * Checks if there is a quasi signal in a bubble.
     *
     * @param pos block position of the block doing the check
     * @return {@code true} if all blocks at the given dist around block has quasi signal, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$hasQuasiNeighborSignalBubble(BlockPos pos);

}

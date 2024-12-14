package ca.fxco.api.pistonlib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Allows to get block's piston behavior.
 * These methods are only used if `usesConfigurablePistonBehavior` returns true
 * This allows for more configurable and conditional piston behavior
 * @author Space Walker
 * @since 1.0.4
 */
public interface BlockPistonBehavior {

    int pl$getWeight(BlockState state);

    /**
     * This must return true in order for the configurable piston behavior to be used!
     * @return true if block uses configurable piston behavior
     * @since 1.0.4
     */
    boolean pl$usesConfigurablePistonBehavior();

    /** If the block is currently movable, for quick checks to boost performance by skipping more intensive checks early
     * However this is not always checked first in some instances, so make sure to account for that!
     * @param level of the block
     * @param pos block position of the block
     * @param state block state of the block
     * @return true if block is movable
     * @since 1.0.4
     */
    boolean pl$isMovable(Level level, BlockPos pos, BlockState state);

    /**
     * @param level of the block
     * @param pos block position of the block
     * @param state block state of the block
     * @param dir direction to move in
     * @return true if block can be pushed by piston
     * @since 1.0.4
     */
    boolean pl$canPistonPush(Level level, BlockPos pos, BlockState state, Direction dir);

    /**
     * @param level of the block
     * @param pos block position of the block
     * @param state block state of the block
     * @param dir direction to move in
     * @return true if block can be pulled by piston
     * @since 1.0.4
     */
    boolean pl$canPistonPull(Level level, BlockPos pos, BlockState state, Direction dir);

    /**
     * @param state of the block
     * @return true if block can bypass fused
     * @since 1.0.4
     */
    boolean pl$canBypassFused(BlockState state);

    /**
     * @param level of the block
     * @param pos block position of the block
     * @param state block state of the block
     * @return true if block can be destroyed by piston
     * @since 1.0.4
     */
    boolean pl$canDestroy(Level level, BlockPos pos, BlockState state);

    /**
     * This is called whenever an entity is pushed into a block by a piston.
     * @param level of the block
     * @param pos block position of the block
     * @param state block state of the block
     * @param entity pushed into the block
     * @since 1.0.4
     */
    void pl$onPushEntityInto(Level level, BlockPos pos, BlockState state, Entity entity);

}

package ca.fxco.api.pistonlib.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * This interface is for internal use only. Use ConfigurablePistonBehavior for single block conditions
 * @author FX
 * @since 1.0.4
 */
public interface BlockStatePistonBehavior {

    /**
     * @return the weight of the block
     * @since 1.0.4
     */
    int pl$getWeight();

    // These methods are only used if `usesConfigurablePistonBehavior` return true
    // This allows for more configurable & conditional piston behavior

    /**
     * This must return true in order for the configurable piston behavior to be used!
     * @return true if block uses configurable piston behavior
     * @since 1.0.4
     */
    boolean pl$usesConfigurablePistonBehavior();

    /** If the block state is currently movable, for quick checks to boost performance by skipping more intensive checks early
     * However this is not always checked first in some instances, so make sure to account for that!
     * @param level of the block
     * @param pos block position of the block
     * @return true if block state is movable
     * @since 1.0.4
     */
    boolean pl$isMovable(Level level, BlockPos pos);

    /**
     * @param level of the block state
     * @param pos block position of the block state
     * @param dir direction to move in
     * @return true if block state can be pushed by piston
     * @since 1.0.4
     */
    boolean pl$canPistonPush(Level level, BlockPos pos, Direction dir);

    /**
     * @param level of the block state
     * @param pos block position of the block state
     * @param dir direction to move in
     * @return true if block state can be pulled by piston
     * @since 1.0.4
     */
    boolean pl$canPistonPull(Level level, BlockPos pos, Direction dir);

    /**
     * @return true if block state can bypass fused
     * @since 1.0.4
     */
    boolean pl$canBypassFused();

    /**
     * @param level of the block state
     * @param pos block position of the block state
     * @return true if block state can be destroyed by piston
     * @since 1.0.4
     */
    boolean pl$canDestroy(Level level, BlockPos pos);

    /**
     * This is called whenever an entity is pushed into a block by a piston.
     * @param level of the block state
     * @param pos block position of the block state
     * @param entity pushed into the block state
     * @since 1.0.4
     */
    void pl$onPushEntityInto(Level level, BlockPos pos, Entity entity);

}

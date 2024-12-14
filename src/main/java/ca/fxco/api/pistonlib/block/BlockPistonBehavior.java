package ca.fxco.api.pistonlib.block;

import ca.fxco.pistonlib.pistonLogic.sticky.StickyType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Allows to get block's piston behavior.
 * These methods are only used if {@link  #pl$usesConfigurablePistonBehavior} returns {@code true}.
 * This allows for more configurable and conditional piston behavior
 *
 * @author Space Walker
 * @since 1.0.4
 */
public interface BlockPistonBehavior {

    /**
     * Gets the weight of this block.
     *
     * @return the weight of the block
     * @since 1.0.4
     */
    int pl$getWeight(BlockState state);

    /**
     * This must return true in order for the configurable piston behavior to be used!
     *
     * @return {@code true} if block uses configurable piston behavior, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$usesConfigurablePistonBehavior();

    /** If the block state is currently movable. Allows for quick checks to boost performance by skipping more
     * intensive checks early. However, this is not always checked first in some instances,
     * so make sure to account for that!
     *
     * @param level of the block
     * @param pos   block position of the block
     * @param state block state of the block
     * @return {@code true} if block state is movable, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$isMovable(Level level, BlockPos pos, BlockState state);

    /**
     * Checks if a piston can push this block.
     *
     * @param level of the block state
     * @param pos   block position of the block state
     * @param state block state of the block
     * @param dir   direction to move in
     * @return {@code true} if block state can be pushed by piston, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canPistonPush(Level level, BlockPos pos, BlockState state, Direction dir);

    /**
     * Checks if a piston can pull at a given location and direction.
     *
     * @param level of the block state
     * @param pos   block position of the block state
     * @param state block state of the block
     * @param dir   direction to move in
     * @return {@code true} if block state can be pulled by piston, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canPistonPull(Level level, BlockPos pos, BlockState state, Direction dir);

    /**
     * Checks if this state is able to bypass the {@link StickyType#FUSED} sticky type.
     *
     * @param state of the block
     * @return {@code true} if block state can bypass fused, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canBypassFused(BlockState state);

    /**
     * Checks if this state can be destroyed at this position.
     *
     * @param level of the block state
     * @param pos   block position of the block state
     * @param state block state of the block
     * @return {@code true} if block state can be destroyed by piston, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canDestroy(Level level, BlockPos pos, BlockState state);

    /**
     * This is called whenever an entity is pushed into a block by a piston.
     *
     * @param level  of the block state
     * @param pos    block position of the block state
     * @param state  block state of the block
     * @param entity pushed into the block state
     * @since 1.0.4
     */
    void pl$onPushEntityInto(Level level, BlockPos pos, BlockState state, Entity entity);

}

package ca.fxco.api.pistonlib.block.state;

import java.util.Map;

import ca.fxco.api.pistonlib.block.BlockPistonStickiness;
import org.jetbrains.annotations.Nullable;

import ca.fxco.pistonlib.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.pistonLogic.sticky.StickyType;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This interface is for internal use only.
 * Use {@link BlockPistonStickiness} for single block conditions
 *
 * @author FX
 * @since 1.0.4
 */
public interface BlockStatePistonStickiness {

    /**
     * Defines if this block can stick to the adjacent block.
     * Only use this on sticky blocks.
     *
     * @return sticky group of the block, {@code null} if this block isn't sticky,
     *  or you are using the configurable stickiness.
     * @since 1.0.4
     */
    @Nullable StickyGroup pl$getStickyGroup();

    /**
     * Checks if this state uses a {@link StickyGroup}.
     *
     * @return {@code true} if block has sticky group, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$hasStickyGroup();

    /*
     * These methods are only used if `pl$usesConfigurablePistonStickiness` returns true
     * This allows for more configurable & conditional piston stickiness
     */

    /**
     * This must return true in order for the configurable piston stickiness to be used!
     *
     * @return {@code true} if piston uses configurable piston stickiness, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$usesConfigurablePistonStickiness();

    /**
     * If the block is currently sticky for any side, for quick checks to boost performance by
     * skipping more intensive checks early. For some checks it might just be faster to set this to true!
     *
     * @return {@code true} if the block is sticky, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$isSticky();

    /**
     * Gets a map of the sticky sides of this state.
     *
     * @return a map of directions that are sticky, and their stickyType.
     * @since 1.0.4
     */
    Map<Direction, StickyType> pl$stickySides();

    /**
     * Checks the stickiness of a side of the state.
     *
     * @param dir direction to get stickyType for
     * @return stickyType of the side.
     * @since 1.0.4
     */
    StickyType pl$sideStickiness(Direction dir);

    /**
     * This only gets used if the sticky type is {@link StickyType#CONDITIONAL}.
     *
     * @param neighborState neighbor of this block state
     * @param dir           direction to check
     * @return {@code true} if matches sticky conditions, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$matchesStickyConditions(BlockState neighborState, Direction dir);

}

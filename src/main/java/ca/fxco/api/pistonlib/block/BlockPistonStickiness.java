package ca.fxco.api.pistonlib.block;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import ca.fxco.pistonlib.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.pistonLogic.sticky.StickyType;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Allow to get stickiness of the block
 * @author FX
 * @since 1.0.4
 */
public interface BlockPistonStickiness {

    /**
     * Defines if this block can stick to the adjacent block. Only use this on sticky blocks
     * @return sticky group of the block
     * @since 1.0.4
     */
    @Nullable StickyGroup pl$getStickyGroup();


    /**
     * @return true if block has sticky group
     * @since 1.0.4
     */
    boolean pl$hasStickyGroup();

    /*
     * These methods are only used if `usesConfigurablePistonStickiness` returns true
     * This allows for more configurable & conditional piston stickiness
     */

    /**
     * This must return true in order for the configurable piston stickiness to be used!
     * @return true if piston uses configurable piston stickiness
     * @since 1.0.4
     */
    boolean pl$usesConfigurablePistonStickiness() ;

    /**
     * If the block is currently sticky for any side, for quick checks to boost performance by
     * skipping more intensive checks early. For some checks it might just be faster to set this to true!
     * @param state block state of the block
     * @return true if the block is sticky
     * @since 1.0.4
     */
    boolean pl$isSticky(BlockState state);

    /**
     * @param state block state of the block
     * @return a map of directions that are sticky, and their stickyType.
     * @since 1.0.4
     */
    Map<Direction, StickyType> pl$stickySides(BlockState state);

    /**
     * @param state block state of the block
     * @param dir direction to get stickyType for
     * @return stickyType of the side.
     * @since 1.0.4
     */
    StickyType pl$sideStickiness(BlockState state, Direction dir);

    /**
     * This only gets used if the sticky type is {@linkplain ca.fxco.pistonlib.pistonLogic.sticky.StickyType#CONDITIONAL CONDITIONAL}.
     * @param state block state of the block
     * @param neighborState block state of the block's neighbor
     * @param dir direction to check
     * @return true if matches sticky conditions
     * @since 1.0.4
     */
    boolean pl$matchesStickyConditions(BlockState state, BlockState neighborState, Direction dir);

}

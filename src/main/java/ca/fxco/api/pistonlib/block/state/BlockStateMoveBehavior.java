package ca.fxco.api.pistonlib.block.state;

import ca.fxco.api.pistonlib.block.BlockMoveBehavior;
import ca.fxco.api.pistonlib.pistonLogic.PistonMoveBehavior;

/**
 * This interface is for internal use only.
 * Use {@link BlockMoveBehavior} for single block conditions
 *
 * @author Space Walker
 * @since 1.0.4
 */
public interface BlockStateMoveBehavior {

    /**
     * Checks if it's possible to override this block state's piston move behavior.
     *
     * @return {@code true} if you can override this block's piston move behavior, otherwise {@code false}
     * @since 1.0.4
     */
    default boolean pl$canOverridePistonMoveBehavior() {
        return true;
    }

    /**
     * Sets a block states piston move behavior override.
     *
     * @since 1.0.4
     */
    default void pl$setPistonMoveBehaviorOverride(PistonMoveBehavior override) {}

    /**
     * Gets the current move behavior override used by this block state.
     *
     * @return The piston move behavior override.
     * @since 1.0.4
     */
    default PistonMoveBehavior pl$getPistonMoveBehaviorOverride() {
        return PistonMoveBehavior.DEFAULT;
    }
}

package ca.fxco.pistonlib.api.block;

import ca.fxco.pistonlib.api.pistonLogic.PistonMoveBehavior;

/**
 * Allows you to override any block's piston move behavior.
 *
 * @author Space Walker
 * @since 1.0.4
 */
public interface BlockMoveBehavior {

    /**
     * Checks if it's possible to override this blocks piston move behavior.
     *
     * @return {@code true} if you can override this block's piston move behavior, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canOverridePistonMoveBehavior();

    /**
     * Sets a blocks piston move behavior override.
     *
     * @since 1.0.4
     */
    void pl$setPistonMoveBehaviorOverride(PistonMoveBehavior override);

    /**
     * Gets the current move behavior override used by this block.
     *
     * @return The piston move behavior override.
     * @since 1.0.4
     */
    PistonMoveBehavior pl$getPistonMoveBehaviorOverride();

}

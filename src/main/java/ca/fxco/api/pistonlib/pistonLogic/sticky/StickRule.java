package ca.fxco.api.pistonlib.pistonLogic.sticky;

import ca.fxco.pistonlib.pistonLogic.sticky.StickyGroup;

/**
 * A functional interface to test if two sticky groups can stick together.
 *
 * @author FX
 * @since 1.0.4
 */
@FunctionalInterface
public interface StickRule {

    /**
     * Test if two sticky groups can stick together.
     *
     * @param group1 The first group
     * @param group2 The second group
     * @return {@code true} if they can stick together, otherwise {@code false}
     */
    boolean test(StickyGroup group1, StickyGroup group2);

}

package ca.fxco.api.pistonlib.pistonLogic.sticky;

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
     * @param group    The main group
     * @param adjGroup The adjacent group
     * @return {@code true} if they can stick together, otherwise {@code false}
     */
    boolean test(StickyGroup group, StickyGroup adjGroup);

}

package ca.fxco.pistonlib.api.pistonLogic.sticky;

/**
 * Built-in {@link StickRule}s and related helper methods.
 * 
 * @author FX
 * @since 1.0.4
 */
public class StickRules {

    /**
     * Passes if the adjacent sticky group is equal.
     * 
     * @since 1.0.4
     */
    public static final StickRule STRICT_SAME = Object::equals;
    /**
     * Passes if the adjacent sticky group is not equal.
     * 
     * @since 1.0.4
     */
    public static final StickRule NOT_STRICT_SAME = (group, adjGroup) -> !group.equals(adjGroup);
    /**
     * Passes if the adjacent sticky group or one of its ancestors is equal.
     * 
     * @since 1.0.4
     */
    public static final StickRule INHERIT_SAME = (group, adjGroup) -> {
        if (adjGroup.equals(group)) {
            return true;
        }
        while(adjGroup.getParent() != null) {
            if (adjGroup.getParent().equals(group)) {
                return true;
            }
            adjGroup = adjGroup.getParent();
        }
        return false;
    };
    /**
     * Passes if the adjacent sticky group and its ancestors are not equal.
     */
    public static final StickRule NOT_INHERIT_SAME = (group, adjGroup) -> {
        if (adjGroup.equals(group)) {
            return false;
        }
        while(adjGroup.getParent() != null) {
            if (adjGroup.getParent().equals(group)) {
                return false;
            }
            adjGroup = adjGroup.getParent();
        }
        return true;
    };

    /**
     * Tests whether the two given sticky groups should stick to each other.
     * 
     * @param group1 the first sticky group to be tested
     * @param group2 the second sticky group to be tested
     * @return whether the two sticky groups should stick to each other
     * @since 1.0.4
     */
    public static boolean test(StickyGroup group1, StickyGroup group2) {
        if (group1 == null || group2 == null) {
            return true;
        }
        return group1.test(group2) && group2.test(group1);
    }
}

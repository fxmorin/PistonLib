package ca.fxco.pistonlib.api.pistonLogic.sticky;

import org.jetbrains.annotations.Nullable;

/**
 * A sticky group represents a collection of blocks that are sticky
 * when moved by pistons. Each sticky group has a stick rule that
 * defines how it interacts with other sticky groups.
 * 
 * @author FX
 * @since 1.0.4
 */
public class StickyGroup {

    protected final @Nullable StickyGroup parent;
    protected final StickRule stickRule;

    public StickyGroup(StickRule stickRule) {
        this(null, stickRule);
    }

    public StickyGroup(@Nullable StickyGroup parent, StickRule stickRule) {
        this.parent = parent;
        this.stickRule = stickRule;
    }

    /**
     * Gets the parent of the sticky group.
     *
     * @return The parent sticky group.
     * @since 1.0.4
     */
    public @Nullable StickyGroup getParent() {
        return this.parent;
    }

    /**
     * Gets the stick rule of this group.
     *
     * @return The stick rule.
     * @since 1.0.4
     */
    public StickRule getStickRule() {
        return this.stickRule;
    }

    /**
     * Tests whether this sticky group should stick to the given sticky group.
     * 
     * @param otherGroup another sticky group
     * @return whether the two sticky groups should stick to each other
     * @since 1.0.4
     */
    public boolean test(StickyGroup otherGroup) {
        return this.stickRule.test(this, otherGroup);
    }

    @Override
    public String toString() {
        return "StickyGroup{" + StickyGroups.getId(this) + "}";
    }
}

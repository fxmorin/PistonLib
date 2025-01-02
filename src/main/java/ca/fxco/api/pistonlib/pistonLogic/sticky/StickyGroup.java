package ca.fxco.api.pistonlib.pistonLogic.sticky;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

/**
 * A sticky group represents a collection of blocks that are sticky
 * when moved by pistons. Each sticky group has a stick rule that
 * defines how it interacts with other sticky groups.
 * 
 * @author FX
 * @since 1.0.4
 */
@Getter
public class StickyGroup {

    private final @Nullable StickyGroup parent;
    private final StickRule stickRule;

    public StickyGroup(StickRule stickRule) {
        this(null, stickRule);
    }

    public StickyGroup(@Nullable StickyGroup parent, StickRule stickRule) {
        this.parent = parent;
        this.stickRule = stickRule;
    }

    @Override
    public String toString() {
        return "StickyGroup{" + StickyGroups.getId(this) + "}";
    }

    /**
     * Tests whether this sticky group should stick to the given sticky group.
     * 
     * @param otherGroup another sticky group
     * @return whether the two sticky groups should stick to each other
     */
    public boolean test(StickyGroup otherGroup) {
        return this.stickRule.test(this, otherGroup);
    }
}

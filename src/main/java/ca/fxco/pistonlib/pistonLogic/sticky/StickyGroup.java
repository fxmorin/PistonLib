package ca.fxco.pistonlib.pistonLogic.sticky;

import ca.fxco.api.pistonlib.pistonLogic.sticky.StickRule;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroups;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

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

    public boolean test(StickyGroup stickyGroup) {
        return this.stickRule.test(this, stickyGroup);
    }
}

package ca.fxco.pistonlib.base;

import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroups;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModStickyGroups {

    private static StickyGroup register(String name, StickyGroup group) {
        return StickyGroups.register(id(name), group);
    }

    public static void bootstrap() { }

    public static void validate() {
        ModRegistries.STICKY_GROUP.forEach(group -> {
            try {
                StickyGroup parent = group.getParent();

                while (parent != null) {
                    if (parent == group)
                        throw new IllegalStateException("a sticky group cannot inherit from itself!");
                    parent = parent.getParent();
                }
            } catch (Exception e) {
                throw new IllegalStateException("sticky group " + group + " is invalid!", e);
            }
        });
    }
}

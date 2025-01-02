package ca.fxco.pistonlib.base;

import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroup;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroups;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModStickyGroups {

    private static StickyGroup register(String name, StickyGroup group) {
        return StickyGroups.register(id(name), group);
    }

    public static void bootstrap() { }

    static boolean locked;

    public static void validate() {
        if (!locked) {
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

            locked = true;
        }
    }
}

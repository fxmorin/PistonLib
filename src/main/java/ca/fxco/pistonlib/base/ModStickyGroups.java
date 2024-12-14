package ca.fxco.pistonlib.base;

import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroups;
import ca.fxco.pistonlib.pistonLogic.sticky.StickRules;
import ca.fxco.pistonlib.pistonLogic.sticky.StickyGroup;

import net.minecraft.resources.ResourceLocation;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModStickyGroups {

    public static final StickyGroup SLIME = StickyGroups.register(new ResourceLocation("slime"), new StickyGroup(StickRules.STRICT_SAME));
    public static final StickyGroup HONEY = StickyGroups.register(new ResourceLocation("honey"), new StickyGroup(StickRules.STRICT_SAME));

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

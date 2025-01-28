package ca.fxco.pistonlib.base;

import ca.fxco.pistonlib.api.PistonLibRegistries;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickRules;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroups;
import net.minecraft.resources.ResourceLocation;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModStickyGroups {

    public static final StickyGroup SLIME = register(
            ResourceLocation.withDefaultNamespace("slime"),
            new StickyGroup(StickRules.STRICT_SAME)
    );
    public static final StickyGroup HONEY = register(
            ResourceLocation.withDefaultNamespace("honey"),
            new StickyGroup(StickRules.STRICT_SAME)
    );

    private static StickyGroup register(String name, StickyGroup group) {
        return register(id(name), group);
    }

    private static StickyGroup register(ResourceLocation id, StickyGroup group) {
        return StickyGroups.register(id, group);
    }

    public static void bootstrap() { }

    public static void validate() {
        PistonLibRegistries.STICKY_GROUP.forEach(group -> {
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

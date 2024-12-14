package ca.fxco.api.pistonlib.pistonLogic.sticky;

import ca.fxco.pistonlib.base.ModRegistries;
import ca.fxco.pistonlib.pistonLogic.sticky.StickyGroup;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper methods for registering and querying sticky groups.
 * @author Space Walker
 * @since 1.0.4
 */
public class StickyGroups {

    /**
     * Registers the given sticky group to the given namespaced id.
     * @param id a namespaced id to uniquely identify the sticky group
     * @param group the sticky group to be registered
     * @return the sticky group that was registered
     * @since 1.0.4
     */
    public static StickyGroup register(ResourceLocation id, StickyGroup group) {
        return Registry.register(ModRegistries.STICKY_GROUP, id, group);
    }

    /**
     * Queries the sticky group registered to the given namespaced id.
     * @param id the namespaced id that uniquely identifies the sticky group
     * @return the sticky group registered to the given namespaced id, or
     *         {@code null} if no sticky group is registered to that id
     * @since 1.0.4
     */
    public static StickyGroup get(ResourceLocation id) {
        return ModRegistries.STICKY_GROUP.get(id);
    }

    /**
     * Queries the namespaced id that the given sticky group is registered to.
     * @param group the sticky group of which the id is queried
     * @return the namespaced id that the sticky group is registered to, or
     *         {@code null} if it is not registered
     * @since 1.0.4
     */
    public static ResourceLocation getId(StickyGroup group) {
        return ModRegistries.STICKY_GROUP.getKey(group);
    }
}

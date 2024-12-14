package ca.fxco.api.pistonlib.pistonLogic.families;

import ca.fxco.pistonlib.base.ModRegistries;
import ca.fxco.pistonlib.pistonLogic.families.PistonFamily;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper methods for registering and querying piston families.
 * @author Space Walker
 * @since 1.0.4
 */
public class PistonFamilies {

    /**
     * Registers the given piston family to the given namespaced id.
     * @param id a namespaced id to uniquely identify the piston family
     * @param family the piston family to be registered
     * @return the piston family that was registered
     * @since 1.0.4
     */
    public static PistonFamily register(ResourceLocation id, PistonFamily family) {
        return Registry.register(ModRegistries.PISTON_FAMILY, id, family);
    }

    /**
     * Queries the piston family registered to the given namespaced id.
     * @param id the namespaced id that uniquely identifies the piston family
     * @return the piston family registered to the given namespaced id, or
     *         {@code null} if no piston family is registered to that id
     * @since 1.0.4
     */
    public static PistonFamily get(ResourceLocation id) {
        return ModRegistries.PISTON_FAMILY.get(id);
    }

    /**
     * Queries the namespaced id that the given piston family is registered to.
     * @param family the piston family of which the id is queried
     * @return the namespaced id that the piston family is registered to, or
     *         {@code null} if it is not registered
     * @since 1.0.4
     */
    public static ResourceLocation getId(PistonFamily family) {
        return ModRegistries.PISTON_FAMILY.getKey(family);
    }
}

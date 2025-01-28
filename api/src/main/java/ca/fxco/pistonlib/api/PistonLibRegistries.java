package ca.fxco.pistonlib.api;

import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroup;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * The PistonLib registries.
 *
 * @author FX
 * @since 1.2.0
 */
public class PistonLibRegistries {

    public static final ResourceKey<Registry<PistonFamily>> PISTON_FAMILY_KEY = createRegistryKey("piston_family");
    public static final ResourceKey<Registry<StickyGroup>> STICKY_GROUP_KEY = createRegistryKey("sticky_group");

    public static final Registry<PistonFamily> PISTON_FAMILY = createRegistry(PISTON_FAMILY_KEY);
    public static final Registry<StickyGroup> STICKY_GROUP = createRegistry(STICKY_GROUP_KEY);


    private static <T> ResourceKey<Registry<T>> createRegistryKey(String id) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("pistonlib",id));
    }

    private static <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }
}

package ca.fxco.pistonlib.api;

import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroup;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * The PistonLib registries.
 *
 * @author FX
 * @since 1.2.0
 */
public class PistonLibRegistries {

    public static final Registry<PistonFamily> PISTON_FAMILY = FabricRegistryBuilder.createSimple(
            PistonFamily.class,
            ResourceLocation.fromNamespaceAndPath("pistonlib","piston_family")
    ).buildAndRegister();
    public static final Registry<StickyGroup> STICKY_GROUP = FabricRegistryBuilder.createSimple(
            StickyGroup.class,
            ResourceLocation.fromNamespaceAndPath("pistonlib","sticky_group")
    ).buildAndRegister();
}

package ca.fxco.pistonlib.base;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModDataComponents {

    public static final DataComponentType<ItemStack> DEBUG_WAND_ITEM = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            id("debug_wand_item"),
            DataComponentType.<ItemStack>builder().persistent(ItemStack.CODEC)
                    .networkSynchronized(ItemStack.STREAM_CODEC).build()
    );

    public static void bootstrap() { }

}

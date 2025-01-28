package ca.fxco.pistonlib.base;

import ca.fxco.pistonlib.PistonLibConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModItems {

    public static final BlockItem BASIC_PISTON = registerBlock(ModBlocks.BASIC_PISTON);
    public static final BlockItem BASIC_STICKY_PISTON = registerBlock(ModBlocks.BASIC_STICKY_PISTON);

    private static BlockItem registerBlock(Block block) {
        return registerBlock(block, new Item.Properties());
    }

    private static BlockItem registerBlock(Block block, Item.Properties itemProperties) {
        ResourceKey<Item> resourceKey = ResourceKey.create(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block));
        return register(resourceKey, new BlockItem(block, itemProperties.setId(resourceKey)));
    }

    private static <T extends Item> T register(String name, Function<Item.Properties, T>  item, Item.Properties properties) {
        ResourceKey<Item> resourceKey = ResourceKey.create(Registries.ITEM, id(name));
        return register(resourceKey, item.apply(properties.setId(resourceKey)));
    }

    private static <T extends Item> T register(ResourceKey<Item> id, T item) {
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void bootstrap() { }

}

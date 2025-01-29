package ca.fxco.pistonlib.base;

import ca.fxco.pistonlib.blocks.mergeBlock.MergeBlock;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlock;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonHeadBlock;
import ca.fxco.pistonlib.pistonLogic.controller.VanillaPistonController;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.PistonType;

import java.util.function.Function;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModBlocks {

    public static final BasicPistonBaseBlock BASIC_PISTON = registerPiston(
            "basic_piston",
            properties -> new BasicPistonBaseBlock(new VanillaPistonController(PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock BASIC_STICKY_PISTON = registerPiston(
            "basic_sticky_piston",
            properties -> new BasicPistonBaseBlock(new VanillaPistonController(PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock BASIC_PISTON_HEAD = registerPistonHead(
            "basic_piston_head",
            BasicPistonHeadBlock::new);
    public static final BasicMovingBlock BASIC_MOVING_BLOCK = registerMovingBlock(
            "basic_moving_block",
            BasicMovingBlock::new);

    public static final MergeBlock MERGE_BLOCK = register("merge_block", MergeBlock::new, Blocks.MOVING_PISTON);

    private static <T extends Block> T register(String name, Function<Properties, T> block, Block propertySource) {
        return register(name, block, Properties.ofFullCopy(propertySource));
    }

    private static <T extends Block> T registerPiston(String name, Function<Properties, T> block) {
        return register(name, block, Properties.ofFullCopy(Blocks.PISTON));
    }

    private static <T extends Block> T registerPistonHead(String name, Function<Properties, T> block) {
        return register(name, block, Properties.ofFullCopy(Blocks.PISTON_HEAD));
    }

    private static <T extends Block> T registerMovingBlock(String name, Function<Properties, T> block) {
        return register(name, block, BasicMovingBlock.createDefaultSettings());
    }

    private static <T extends Block> T register(String name, Function<Properties, T> function, Properties properties) {
        ResourceKey<Block> resourceKey = ResourceKey.create(Registries.BLOCK, id(name));
        T block = function.apply(properties.setId(resourceKey));
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }

    public static void bootstrap() { }
}

package ca.fxco.pistonlib.base;

import java.util.Map;
import java.util.function.Function;

import ca.fxco.pistonlib.PistonLibConfig;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyType;
import ca.fxco.pistonlib.blocks.*;
import ca.fxco.pistonlib.blocks.autoCraftingBlock.AutoCraftingBlock;
import ca.fxco.pistonlib.blocks.halfBlocks.HalfHoneyBlock;
import ca.fxco.pistonlib.blocks.halfBlocks.HalfObsidianBlock;
import ca.fxco.pistonlib.blocks.halfBlocks.HalfPoweredBlock;
import ca.fxco.pistonlib.blocks.halfBlocks.HalfRedstoneLampBlock;
import ca.fxco.pistonlib.blocks.halfBlocks.HalfSlimeBlock;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlock;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonArmBlock;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonHeadBlock;
import ca.fxco.pistonlib.blocks.pistons.configurablePiston.ConfigurableMovingBlock;
import ca.fxco.pistonlib.blocks.pistons.configurablePiston.ConfigurablePistonBaseBlock;
import ca.fxco.pistonlib.blocks.pistons.configurablePiston.ConfigurablePistonHeadBlock;
import ca.fxco.pistonlib.blocks.pistons.longPiston.LongPistonHeadBlock;
import ca.fxco.pistonlib.blocks.mergeBlock.MergeBlock;
import ca.fxco.pistonlib.blocks.pistons.movableBlockEntities.MBEMovingBlock;
import ca.fxco.pistonlib.blocks.pistons.slipperyPiston.SlipperyMovingBlock;
import ca.fxco.pistonlib.blocks.pistons.slipperyPiston.SlipperyPistonBaseBlock;
import ca.fxco.pistonlib.blocks.pistons.slipperyPiston.SlipperyPistonHeadBlock;
import ca.fxco.pistonlib.blocks.pistons.veryStickyPiston.StickyPistonHeadBlock;
import ca.fxco.pistonlib.blocks.pistons.veryStickyPiston.VeryStickyPistonBaseBlock;
import ca.fxco.pistonlib.blocks.slipperyBlocks.BaseSlipperyBlock;
import ca.fxco.pistonlib.blocks.slipperyBlocks.SlipperyRedstoneBlock;
import ca.fxco.pistonlib.blocks.slipperyBlocks.SlipperySlimeBlock;
import ca.fxco.pistonlib.pistonLogic.controller.*;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;

import static ca.fxco.pistonlib.base.ModPistonFamilies.*;
import static ca.fxco.pistonlib.PistonLib.id;

public class ModBlocks {

    // Half Blocks
    public static final Block HALF_SLIME_BLOCK = register("half_slime", HalfSlimeBlock::new, Blocks.SLIME_BLOCK);
    public static final Block HALF_HONEY_BLOCK = register("half_honey", HalfHoneyBlock::new, Blocks.HONEY_BLOCK);
    public static final Block HALF_REDSTONE_BLOCK = register("half_redstone", HalfPoweredBlock::new, Blocks.REDSTONE_BLOCK);
    public static final Block HALF_OBSIDIAN_BLOCK = register("half_obsidian", HalfObsidianBlock::new, Blocks.OBSIDIAN);
    public static final Block HALF_REDSTONE_LAMP_BLOCK = register("half_redstone_lamp", HalfRedstoneLampBlock::new, Blocks.REDSTONE_LAMP);

    // Create Custom Blocks
    public static final Block DRAG_BLOCK = register("drag_block", PullOnlyBlock::new, Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(22.0f).destroyTime(18.0f));
    public static final Block STICKYLESS_BLOCK = register("stickyless_block", StickylessBlock::new, Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).strength(64.0f).destroyTime(64.0f));
    public static final Block STICKY_TOP_BLOCK = register("sticky_top_block", properties -> new StickySidesBlock(properties, Map.of(Direction.UP, StickyType.STICKY)), Blocks.STONE);
    public static final Block SLIMY_REDSTONE_BLOCK = register("slimy_redstone_block", SlimyPoweredBlock::new, Properties.ofFullCopy(Blocks.REDSTONE_BLOCK).noOcclusion());
    public static final Block ALL_SIDED_OBSERVER = register("all_sided_observer", AllSidedObserverBlock::new, Blocks.OBSERVER);
    public static final Block GLUE_BLOCK = register("glue_block", GlueBlock::new, Blocks.END_STONE);
    public static final Block POWERED_STICKY_BLOCK = register("powered_sticky_block", PoweredStickyBlock::new, Blocks.OAK_PLANKS);
    public static final Block STICKY_CHAIN_BLOCK = register("sticky_chain", StickyChainBlock::new, Blocks.CHAIN);
    public static final Block AXIS_LOCKED_BLOCK = register("axis_locked_block", AxisLockedBlock::new, Blocks.DEEPSLATE_BRICKS);
    public static final Block MOVE_COUNTING_BLOCK = register("move_counting_block", MoveCountingBlock::new, Blocks.SCULK);
    public static final Block WEAK_REDSTONE_BLOCK = register("weak_redstone_block", WeakPoweredBlock::new, Blocks.REDSTONE_BLOCK);
    public static final Block QUASI_BLOCK = register("quasi_block", QuasiBlock::new, Blocks.REDSTONE_BLOCK);
    public static final Block ERASE_BLOCK = register("erase_block", EraseBlock::new, Blocks.REDSTONE_BLOCK);
    public static final Block HEAVY_BLOCK = register("heavy_block", properties -> new WeightBlock(properties, 2), Blocks.IRON_BLOCK);

    // Slippery Blocks
    // These blocks if they are not touching a solid surface
    public static final Block SLIPPERY_SLIME_BLOCK = register("slippery_slime_block", SlipperySlimeBlock::new, Blocks.SLIME_BLOCK);
    public static final Block SLIPPERY_REDSTONE_BLOCK = register("slippery_redstone_block", SlipperyRedstoneBlock::new, Blocks.REDSTONE_BLOCK);
    public static final Block SLIPPERY_STONE_BLOCK = register("slippery_stone_block", BaseSlipperyBlock::new, Blocks.STONE);

    // Obsidian Blocks
    public static final Block OBSIDIAN_SLAB_BLOCK = register("obsidian_slab_block", ObsidianSlabBlock::new, Blocks.OBSIDIAN);
    public static final Block OBSIDIAN_STAIR_BLOCK = register("obsidian_stair_block", properties -> new StairBlock(Blocks.OBSIDIAN.defaultBlockState(), properties), Properties.ofFullCopy(Blocks.OBSIDIAN));

    // Piston Blocks should always be initialized in the following order:
    // base piston blocks, Piston Arms, Piston heads, Moving Pistons

    // Basic Piston
    // Acts exactly like a normal vanilla piston
    public static final BasicPistonBaseBlock BASIC_PISTON = registerPiston("basic_piston", properties -> new BasicPistonBaseBlock(new VanillaPistonController(BASIC, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock BASIC_STICKY_PISTON = registerPiston("basic_sticky_piston", properties -> new BasicPistonBaseBlock(new VanillaPistonController(BASIC, PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock BASIC_PISTON_HEAD = registerPistonHead("basic_piston_head", properties -> new BasicPistonHeadBlock(BASIC, properties));
    public static final BasicMovingBlock BASIC_MOVING_BLOCK = registerMovingBlock("basic_moving_block", properties -> new BasicMovingBlock(BASIC, properties));

    // Configurable Piston - Testing only
    // The one and only configurable piston. It can do mostly everything that the other pistons can do, allowing you
    // to very easily enable and disable features in your pistons
    public static final BasicPistonBaseBlock CONFIGURABLE_PISTON = registerPiston("configurable_piston", properties -> new ConfigurablePistonBaseBlock(new ConfigurablePistonController(CONFIGURABLE, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock CONFIGURABLE_STICKY_PISTON = registerPiston("configurable_sticky_piston", properties -> new ConfigurablePistonBaseBlock(new ConfigurablePistonController(CONFIGURABLE, PistonType.STICKY), properties));
    public static final BasicPistonArmBlock CONFIGURABLE_PISTON_ARM = registerPistonHead("configurable_piston_arm", properties -> new BasicPistonArmBlock(CONFIGURABLE, properties));
    public static final BasicPistonHeadBlock CONFIGURABLE_PISTON_HEAD = registerPistonHead("configurable_piston_head", properties -> new ConfigurablePistonHeadBlock(CONFIGURABLE, properties));
    public static final ConfigurableMovingBlock CONFIGURABLE_MOVING_BLOCK = registerMovingBlock("configurable_moving_block", properties -> new ConfigurableMovingBlock(CONFIGURABLE, properties));

    // Basic Long Piston
    // Can extend further than 1 block
    public static final BasicPistonBaseBlock LONG_PISTON = registerPiston("long_piston", properties -> new BasicPistonBaseBlock(new LongPistonController(LONG, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock LONG_STICKY_PISTON = registerPiston("long_sticky_piston", properties -> new BasicPistonBaseBlock(new LongPistonController(LONG, PistonType.STICKY), properties));
    public static final BasicPistonArmBlock LONG_PISTON_ARM = registerPistonHead("long_piston_arm", properties -> new BasicPistonArmBlock(LONG, properties));
    public static final LongPistonHeadBlock LONG_PISTON_HEAD = registerPistonHead("long_piston_head", properties -> new LongPistonHeadBlock(LONG, properties));
    public static final BasicMovingBlock LONG_MOVING_BLOCK = registerMovingBlock("long_moving_block", properties -> new BasicMovingBlock(LONG, properties));

    // Stale Piston
    // A vanilla piston except it cannot be quasi-powered
    public static final BasicPistonBaseBlock STALE_PISTON = registerPiston("stale_piston", properties -> new BasicPistonBaseBlock(new StalePistonController(STALE, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock STALE_STICKY_PISTON = registerPiston("stale_sticky_piston", properties -> new BasicPistonBaseBlock(new StalePistonController(STALE, PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock STALE_PISTON_HEAD = registerPistonHead("stale_piston_head", properties -> new BasicPistonHeadBlock(STALE, properties));
    public static final BasicMovingBlock STALE_MOVING_BLOCK = registerMovingBlock("stale_moving_block", properties -> new BasicMovingBlock(STALE, properties));

    // Very Quasi Piston
    // A vanilla piston except it can be quasi-powered from 5 blocks up
    public static final BasicPistonBaseBlock VERY_QUASI_PISTON = registerPiston("very_quasi_piston", properties -> new BasicPistonBaseBlock(new VeryQuasiPistonController(VERY_QUASI, PistonType.DEFAULT, 5), properties));
    public static final BasicPistonBaseBlock VERY_QUASI_STICKY_PISTON = registerPiston("very_quasi_sticky_piston", properties -> new BasicPistonBaseBlock(new VeryQuasiPistonController(VERY_QUASI, PistonType.STICKY, 5), properties));
    public static final BasicPistonHeadBlock VERY_QUASI_PISTON_HEAD = registerPistonHead("very_quasi_piston_head", properties -> new BasicPistonHeadBlock(VERY_QUASI, properties));
    public static final BasicMovingBlock VERY_QUASI_MOVING_BLOCK = registerMovingBlock("very_quasi_moving_block", properties -> new BasicMovingBlock(VERY_QUASI, properties));

    // Strong Piston
    // Can push 24 blocks, although it takes a lot longer to push (0.05x slower)
    public static final BasicPistonBaseBlock STRONG_PISTON = registerPiston("strong_piston", properties -> new BasicPistonBaseBlock(new VanillaPistonController(STRONG, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock STRONG_STICKY_PISTON = registerPiston("strong_sticky_piston", properties -> new BasicPistonBaseBlock(new VanillaPistonController(STRONG, PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock STRONG_PISTON_HEAD = registerPistonHead("strong_piston_head", properties -> new BasicPistonHeadBlock(STRONG, properties));
    public static final BasicMovingBlock STRONG_MOVING_BLOCK = registerMovingBlock("strong_moving_block", properties -> new BasicMovingBlock(STRONG, properties));

    // Fast Piston
    // Can only push 2 block, although it's very fast
    public static final BasicPistonBaseBlock FAST_PISTON = registerPiston("fast_piston", properties -> new BasicPistonBaseBlock(new VanillaPistonController(FAST, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock FAST_STICKY_PISTON = registerPiston("fast_sticky_piston", properties -> new BasicPistonBaseBlock(new VanillaPistonController(FAST, PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock FAST_PISTON_HEAD = registerPistonHead("fast_piston_head", properties -> new BasicPistonHeadBlock(FAST, properties));
    public static final BasicMovingBlock FAST_MOVING_BLOCK = registerMovingBlock("fast_moving_block", properties -> new BasicMovingBlock(FAST, properties));

    // Very Sticky Piston
    // It's face acts like a slime block, it can be pulled while extended.
    // Doing so will pull the moving piston with it as it retracts
    public static final BasicPistonBaseBlock VERY_STICKY_PISTON = registerPiston("very_sticky_piston", properties -> new VeryStickyPistonBaseBlock(new VanillaPistonController(VERY_STICKY, PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock STICKY_PISTON_HEAD = registerPistonHead("very_sticky_piston_head", properties -> new StickyPistonHeadBlock(VERY_STICKY, properties));
    public static final BasicMovingBlock STICKY_MOVING_BLOCK = registerMovingBlock("very_sticky_moving_block", properties -> new BasicMovingBlock(VERY_STICKY, properties));


    // Front Powered Piston
    // Normal piston but can be powered through the front
    public static final BasicPistonBaseBlock FRONT_POWERED_PISTON = registerPiston("front_powered_piston", properties -> new BasicPistonBaseBlock(new FrontPoweredPistonController(FRONT_POWERED, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock FRONT_POWERED_STICKY_PISTON = registerPiston("front_powered_sticky_piston", properties -> new BasicPistonBaseBlock(new FrontPoweredPistonController(FRONT_POWERED, PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock FRONT_POWERED_PISTON_HEAD = registerPistonHead("front_powered_piston_head", properties -> new BasicPistonHeadBlock(FRONT_POWERED, properties));
    public static final BasicMovingBlock FRONT_POWERED_MOVING_BLOCK = registerMovingBlock("front_powered_moving_block", properties -> new BasicMovingBlock(FRONT_POWERED, properties));


    // Slippery Piston
    // It's just a normal piston except its slippery
    public static final BasicPistonBaseBlock SLIPPERY_PISTON = registerPiston("slippery_piston", properties -> new SlipperyPistonBaseBlock(new VanillaPistonController(SLIPPERY, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock SLIPPERY_STICKY_PISTON = registerPiston("slippery_sticky_piston", properties -> new SlipperyPistonBaseBlock(new VanillaPistonController(SLIPPERY, PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock SLIPPERY_PISTON_HEAD = registerPistonHead("slippery_piston_head", properties -> new SlipperyPistonHeadBlock(SLIPPERY, properties));
    public static final SlipperyMovingBlock SLIPPERY_MOVING_BLOCK = registerMovingBlock("slippery_moving_block", properties -> new SlipperyMovingBlock(SLIPPERY, properties));

    // Super Piston
    // What's push limit? What is super sticky?
    public static final BasicPistonBaseBlock SUPER_PISTON = registerPiston("super_piston", properties -> new BasicPistonBaseBlock(new VanillaPistonController(SUPER, PistonType.DEFAULT), properties));
    public static final BasicPistonBaseBlock SUPER_STICKY_PISTON = registerPiston("super_sticky_piston", properties -> new BasicPistonBaseBlock(new VanillaPistonController(SUPER, PistonType.STICKY), properties));
    public static final BasicPistonHeadBlock SUPER_PISTON_HEAD = registerPistonHead("super_piston_head", properties -> new BasicPistonHeadBlock(SUPER, properties));
    public static final BasicMovingBlock SUPER_MOVING_BLOCK = registerMovingBlock("super_moving_block", properties -> new BasicMovingBlock(SUPER, properties));

    // MBE Piston
    // A piston that can move block entities
    public static final BasicPistonBaseBlock MBE_PISTON = registerPiston("mbe_piston", createMBEPistonBlock(PistonType.DEFAULT));
    public static final BasicPistonBaseBlock MBE_STICKY_PISTON = registerPiston("mbe_sticky_piston", createMBEPistonBlock(PistonType.STICKY));
    public static final BasicPistonHeadBlock MBE_PISTON_HEAD_BLOCK = register("mbe_piston_head", properties -> new BasicPistonHeadBlock(MBE, properties), Properties.ofFullCopy(Blocks.PISTON_HEAD));
    public static final MBEMovingBlock MBE_MOVING_BLOCK = registerMovingBlock("mbe_moving_block", properties -> new MBEMovingBlock(MBE, properties));

    public static final MergeBlock MERGE_BLOCK = register("merge_block", MergeBlock::new, Blocks.MOVING_PISTON);

    public static final AutoCraftingBlock AUTO_CRAFTING_BLOCK = register("auto_crafting_block", AutoCraftingBlock::new, Properties.ofFullCopy(Blocks.CRAFTING_TABLE).pl$setDisabled(() -> !PistonLibConfig.autoCraftingBlock));

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

    private static Function<Properties, BasicPistonBaseBlock> createMBEPistonBlock(PistonType type) {
        return properties -> new BasicPistonBaseBlock(new VanillaPistonController(MBE, type) {
            @Override
            public boolean canMoveBlock(BlockState state) {
                return true;
            }
        }, properties);
    }
}

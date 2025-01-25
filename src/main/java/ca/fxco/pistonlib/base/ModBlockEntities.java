package ca.fxco.pistonlib.base;

import ca.fxco.pistonlib.api.blockEntity.PLBlockEntities;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.api.pistonLogic.structure.StructureGroup;
import ca.fxco.pistonlib.blocks.autoCraftingBlock.AutoCraftingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.configurablePiston.ConfigurableMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.fastPiston.FastMovingBlockEntity;
import ca.fxco.pistonlib.blocks.mergeBlock.MergeBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.movableBlockEntities.MBEMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.speedPiston.SpeedMovingBlockEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModBlockEntities {

    // Pistons
    public static final BlockEntityType<BasicMovingBlockEntity> BASIC_MOVING_BLOCK_ENTITY;
    public static final BlockEntityType<ConfigurableMovingBlockEntity> CONFIGURABLE_MOVING_BLOCK_ENTITY;
    public static final BlockEntityType<SpeedMovingBlockEntity> SPEED_MOVING_BLOCK_ENTITY;
    public static final BlockEntityType<FastMovingBlockEntity> FAST_MOVING_BLOCK_ENTITY;
    public static final BlockEntityType<MBEMovingBlockEntity> MBE_MOVING_BLOCK_ENTITY;

    // Other
    public static final BlockEntityType<MergeBlockEntity> MERGE_BLOCK_ENTITY;
    public static final BlockEntityType<AutoCraftingBlockEntity> AUTO_CRAFTING_BLOCK_ENTITY;

    static {
        // Pistons
        BASIC_MOVING_BLOCK_ENTITY = register(
                "basic",
                BasicMovingBlockEntity::new
        );
        CONFIGURABLE_MOVING_BLOCK_ENTITY = register(
                "configurable",
                ConfigurableMovingBlockEntity::new
        );
        SPEED_MOVING_BLOCK_ENTITY = register(
                "speed",
                SpeedMovingBlockEntity::new
        );
        FAST_MOVING_BLOCK_ENTITY = register(
                "fast",
                FastMovingBlockEntity::new
        );
        MBE_MOVING_BLOCK_ENTITY = register(
                "mbe",
                MBEMovingBlockEntity::new
        );

        // Other
        MERGE_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                id("merge"),
                FabricBlockEntityTypeBuilder.create(MergeBlockEntity::new, ModBlocks.MERGE_BLOCK).build(null)
        );
        AUTO_CRAFTING_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                id("auto_crafting_block"),
                FabricBlockEntityTypeBuilder.create(AutoCraftingBlockEntity::new, ModBlocks.AUTO_CRAFTING_BLOCK).build(null)
        );
    }

    private static <T extends BasicMovingBlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<T> factory
    ) {
        return PLBlockEntities.register(id(name+"_moving_block"), factory);
    }

    public static void bootstrap() { }

    @FunctionalInterface
    public interface Factory<T extends PistonMovingBlockEntity> {

        T create(PistonFamily family, StructureGroup structureGroup, BlockPos pos, BlockState state,
                 BlockState movedState, BlockEntity movedBlockEntity, Direction facing, boolean extending,
                 boolean isSourcePiston);

    }

}

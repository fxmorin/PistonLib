package ca.fxco.pistonlib.base;

import ca.fxco.pistonlib.api.pistonLogic.families.PistonBehavior;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamilies;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamilyMember;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.configurablePiston.ConfigurableMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.fastPiston.FastMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.movableBlockEntities.MBEMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.speedPiston.SpeedMovingBlockEntity;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.properties.PistonType;

import java.util.Objects;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModPistonFamilies {

    public static final PistonFamily VANILLA = register("vanilla", PistonFamily.builder()
            .behavior(PistonBehavior.DEFAULT)
            .base(PistonType.DEFAULT, Blocks.PISTON)
            .base(PistonType.STICKY, Blocks.STICKY_PISTON)
            .head(Blocks.PISTON_HEAD)
            .moving(Blocks.MOVING_PISTON)
            .vanillaMovingBlockEntity(BlockEntityType.PISTON, PistonMovingBlockEntity::new));

    public static final PistonFamily BASIC = register("basic", PistonFamily.builder()
            .behavior(PistonBehavior.DEFAULT)
            .base(PistonType.DEFAULT, ModBlocks.BASIC_PISTON)
            .base(PistonType.STICKY, ModBlocks.BASIC_STICKY_PISTON)
            .head(ModBlocks.BASIC_PISTON_HEAD)
            .moving(ModBlocks.BASIC_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.BASIC_MOVING_BLOCK_ENTITY, BasicMovingBlockEntity::new));

    public static final PistonFamily LONG = register("long", PistonFamily.builder()
            .behavior(PistonBehavior.builder()
                    .maxLength(12)
                    .noQuasi())
            .base(PistonType.DEFAULT, ModBlocks.LONG_PISTON)
            .base(PistonType.STICKY, ModBlocks.LONG_STICKY_PISTON)
            .arm(ModBlocks.LONG_PISTON_ARM)
            .head(ModBlocks.LONG_PISTON_HEAD)
            .moving(ModBlocks.LONG_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.BASIC_MOVING_BLOCK_ENTITY, BasicMovingBlockEntity::new));

    public static final PistonFamily CONFIGURABLE = register("configurable", PistonFamily.builder()
            .behavior(PistonBehavior.builder()
                    .maxLength(2)
                    .noQuasi()
                    .extendingSpeed(0.1F)
                    .retractingSpeed(0.5F))
            .base(PistonType.DEFAULT, ModBlocks.CONFIGURABLE_PISTON)
            .base(PistonType.STICKY, ModBlocks.CONFIGURABLE_STICKY_PISTON)
            .arm(ModBlocks.CONFIGURABLE_PISTON_ARM)
            .head(ModBlocks.CONFIGURABLE_PISTON_HEAD)
            .moving(ModBlocks.CONFIGURABLE_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.CONFIGURABLE_MOVING_BLOCK_ENTITY, ConfigurableMovingBlockEntity::new));

    public static final PistonFamily STALE = register("stale", PistonFamily.builder()
            .behavior(PistonBehavior.builder()
                    .noQuasi())
            .base(PistonType.DEFAULT, ModBlocks.STALE_PISTON)
            .base(PistonType.STICKY, ModBlocks.STALE_STICKY_PISTON)
            .head(ModBlocks.STALE_PISTON_HEAD)
            .moving(ModBlocks.STALE_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.BASIC_MOVING_BLOCK_ENTITY, BasicMovingBlockEntity::new));

    public static final PistonFamily VERY_QUASI = register("very_quasi", PistonFamily.builder()
            .behavior(PistonBehavior.DEFAULT)
            .base(PistonType.DEFAULT, ModBlocks.VERY_QUASI_PISTON)
            .base(PistonType.STICKY, ModBlocks.VERY_QUASI_STICKY_PISTON)
            .head(ModBlocks.VERY_QUASI_PISTON_HEAD)
            .moving(ModBlocks.VERY_QUASI_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.BASIC_MOVING_BLOCK_ENTITY, BasicMovingBlockEntity::new));

    public static final PistonFamily STRONG = register("strong", PistonFamily.builder()
            .behavior(PistonBehavior.builder()
                    .speed(0.05F)
                    .pushLimit(24))
            .base(PistonType.DEFAULT, ModBlocks.STRONG_PISTON)
            .base(PistonType.STICKY, ModBlocks.STRONG_STICKY_PISTON)
            .head(ModBlocks.STRONG_PISTON_HEAD)
            .moving(ModBlocks.STRONG_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.SPEED_MOVING_BLOCK_ENTITY, SpeedMovingBlockEntity::new)
            .customTextures(true));

    public static final PistonFamily FAST = register("fast", PistonFamily.builder()
            .behavior(PistonBehavior.builder()
                    .pushLimit(2))
            .base(PistonType.DEFAULT, ModBlocks.FAST_PISTON)
            .base(PistonType.STICKY, ModBlocks.FAST_STICKY_PISTON)
            .head(ModBlocks.FAST_PISTON_HEAD)
            .moving(ModBlocks.FAST_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.FAST_MOVING_BLOCK_ENTITY, FastMovingBlockEntity::new));

    public static final PistonFamily FRONT_POWERED = register("front_powered", PistonFamily.builder()
            .behavior(PistonBehavior.builder()
                    .frontPowered())
            .base(PistonType.DEFAULT, ModBlocks.FRONT_POWERED_PISTON)
            .base(PistonType.STICKY, ModBlocks.FRONT_POWERED_STICKY_PISTON)
            .head(ModBlocks.FRONT_POWERED_PISTON_HEAD)
            .moving(ModBlocks.FRONT_POWERED_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.BASIC_MOVING_BLOCK_ENTITY, BasicMovingBlockEntity::new));

    public static final PistonFamily SLIPPERY = register("slippery", PistonFamily.builder()
            .behavior(PistonBehavior.DEFAULT)
            .base(PistonType.DEFAULT, ModBlocks.SLIPPERY_PISTON)
            .base(PistonType.STICKY, ModBlocks.SLIPPERY_STICKY_PISTON)
            .head(ModBlocks.SLIPPERY_PISTON_HEAD)
            .moving(ModBlocks.SLIPPERY_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.BASIC_MOVING_BLOCK_ENTITY, BasicMovingBlockEntity::new));

    public static final PistonFamily SUPER = register("super", PistonFamily.builder()
            .behavior(PistonBehavior.builder()
                    .pushLimit(Integer.MAX_VALUE)
                    .verySticky())
            .base(PistonType.DEFAULT, ModBlocks.SUPER_PISTON)
            .base(PistonType.STICKY, ModBlocks.SUPER_STICKY_PISTON)
            .head(ModBlocks.SUPER_PISTON_HEAD)
            .moving(ModBlocks.SUPER_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.BASIC_MOVING_BLOCK_ENTITY, BasicMovingBlockEntity::new));

    public static final PistonFamily MBE = register("mbe", PistonFamily.builder()
            .behavior(PistonBehavior.DEFAULT)
            .base(PistonType.DEFAULT, ModBlocks.MBE_PISTON)
            .base(PistonType.STICKY, ModBlocks.MBE_STICKY_PISTON)
            .head(ModBlocks.MBE_PISTON_HEAD_BLOCK)
            .moving(ModBlocks.MBE_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.MBE_MOVING_BLOCK_ENTITY, MBEMovingBlockEntity::new));

    public static final PistonFamily VERY_STICKY = register("very_sticky", PistonFamily.builder()
            .behavior(PistonBehavior.builder()
                    .verySticky())
            .base(PistonType.STICKY, ModBlocks.VERY_STICKY_PISTON)
            .head(ModBlocks.STICKY_PISTON_HEAD)
            .moving(ModBlocks.STICKY_MOVING_BLOCK)
            .movingBlockEntity(ModBlockEntities.BASIC_MOVING_BLOCK_ENTITY, BasicMovingBlockEntity::new));

    private static PistonFamily register(String name, PistonFamily.PistonFamilyBuilder familyBuilder) {
        return register(name, familyBuilder.build());
    }

    private static PistonFamily register(String name, PistonFamily family) {
        return PistonFamilies.register(id(name), family);
    }

    public static void bootstrap() { }

    public static void validate() {
        ModRegistries.PISTON_FAMILY.forEach(family -> {
            try {
                if (family.getMinLength() > family.getMaxLength()) {
                    throw new IllegalStateException("min length is greater than max length");
                }

                if (family.getBases().isEmpty()) {
                    throw new IllegalStateException("missing base block");
                }
                Objects.requireNonNull(family.getHead(), "head block");
                if (family.getMaxLength() > 1) {
                    Objects.requireNonNull(family.getArm(), "missing arm block");
                }
                Objects.requireNonNull(family.getMoving(), "moving block");
                Objects.requireNonNull(family.getMovingBlockEntityType(), "moving block entity type");
                Objects.requireNonNull(family.getMovingBlockEntityFactory(), "moving block entity factory");
            } catch (Exception e) {
                throw new IllegalStateException("piston family " + family + " is invalid!", e);
            }

            family.getBases().forEach((type, base) -> ((PistonFamilyMember) base).setFamily(family));
            if (family.getArm() != null) {
                ((PistonFamilyMember) family.getArm()).setFamily(family);
            }
            ((PistonFamilyMember) family.getHead()).setFamily(family);
            ((PistonFamilyMember) family.getMoving()).setFamily(family);

            family.getMovingBlockEntityType().addSupportedBlock(family.getMoving());
        });

        if (FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment()) {
            BuiltInRegistries.BLOCK.entrySet().forEach(entry -> {
                if (entry.getValue() instanceof PistonFamilyMember familyMember && familyMember.getFamily() == null) {
                    throw new IllegalStateException("Missing piston family for: " + entry.getValue());
                }
            });
        }
    }
}

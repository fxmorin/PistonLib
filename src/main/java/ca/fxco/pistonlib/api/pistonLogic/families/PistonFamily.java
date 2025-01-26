package ca.fxco.pistonlib.api.pistonLogic.families;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.api.pistonLogic.structure.StructureGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;

/**
 * A piston family is a collection of blocks and a block entity type
 * that form a set a pistons that act according to the piston behavior
 * as defined in the family.
 * <p>
 * A piston family holds references to piston base blocks for each
 * {@link net.minecraft.world.level.block.state.properties.PistonType
 * PistonType}, a piston head block, a moving block, a moving block
 * entity type, and a moving block entity factory. If the max length
 * of the pistons as defined in the piston behavior is more than 1,
 * the family must also hold a reference to a piston arm block.
 * 
 * @author FX
 * @since 1.0.4
 */
@Builder
@AllArgsConstructor
public class PistonFamily {

    @Delegate(types = PistonBehavior.class, excludes = PistonBehavior.PistonBehaviorBuilder.class)
    private final PistonBehavior behavior;

    private final Map<PistonType, Block> bases = new EnumMap<>(PistonType.class);
    @Getter
    private final @Nullable Block arm;
    @Getter
    private final Block head;
    @Getter
    private final Block moving;
    @Getter
    private final BlockEntityType<? extends PistonMovingBlockEntity> movingBlockEntityType;
    @Getter
    private final Factory<? extends PistonMovingBlockEntity> movingBlockEntityFactory;

    private final boolean customTextures;

    @Override
    public String toString() {
        return "PistonFamily{" + PistonFamilies.getId(this) + "}";
    }

    public Map<PistonType, Block> getBases() {
        return Collections.unmodifiableMap(this.bases);
    }

    public @Nullable Block getBase(PistonType type) {
        return this.bases.get(type);
    }

    /**
     * @return an arbitrary base block
     */
    public Block getBase() {
        return this.bases.values().iterator().next();
    }

    public boolean hasCustomTextures() {
        return customTextures && PistonLib.DATAGEN_ACTIVE;
    }

    public PistonMovingBlockEntity newMovingBlockEntity(BlockPos pos, BlockState state, BlockState movedState,
                                                       BlockEntity movedBlockEntity, Direction facing,
                                                       boolean extending, boolean isSourcePiston) {
        return this.movingBlockEntityFactory.create(
                this, null,
                pos, state, movedState,
                movedBlockEntity,
                facing, extending, isSourcePiston
        );
    }

    public PistonMovingBlockEntity newMovingBlockEntity(StructureGroup structureGroup, BlockPos pos, BlockState state,
                                                       BlockState movedState, BlockEntity movedBlockEntity,
                                                       Direction facing, boolean extending, boolean isSourcePiston) {
        return this.movingBlockEntityFactory.create(
                this, structureGroup,
                pos, state, movedState,
                movedBlockEntity,
                facing, extending, isSourcePiston
        );
    }

    public boolean hasCustomLength() {
        return this.behavior.getMinLength() != 0 || this.behavior.getMaxLength() != 1;
    }

    public static class PistonFamilyBuilder {

        Map<PistonType, Block> bases = new EnumMap<>(PistonType.class);
        BlockEntityType<? extends PistonMovingBlockEntity> movingBlockEntityType;
        Factory<? extends PistonMovingBlockEntity> movingBlockEntityFactory;

        public PistonFamilyBuilder base(PistonType type, Block base) {
            this.bases.put(type, base);
            return this;
        }

        public PistonFamilyBuilder behavior(PistonBehavior.PistonBehaviorBuilder builder) {
            this.behavior = builder.build();
            return this;
        }

        public <T extends PistonMovingBlockEntity> PistonFamilyBuilder movingBlockEntity(
            BlockEntityType<T> type,
            Factory<T> factory
        ) {
            this.movingBlockEntityType = type;
            this.movingBlockEntityFactory = factory;
            return this;
        }

        public <T extends PistonMovingBlockEntity> PistonFamilyBuilder vanillaMovingBlockEntity(
                BlockEntityType<T> type,
                BiFunction<BlockPos, BlockState, T> factory
        ) {
            this.movingBlockEntityType = type;
            this.movingBlockEntityFactory = (family, structureGroup, pos, state, movedState, movedBlockEntity,
                                             facing, extending, isSourcePiston) -> factory.apply(pos, state);
            return this;
        }
    }

    /**
     * Factory used to create PistonMovingBlockEntities
     *
     * @param <T> The type of the piston moving block entity
     */
    @FunctionalInterface
    public interface Factory<T extends PistonMovingBlockEntity> {

        T create(PistonFamily family, StructureGroup structureGroup, BlockPos pos, BlockState state,
                 BlockState movedState, BlockEntity movedBlockEntity, Direction facing, boolean extending,
                 boolean isSourcePiston);
    }
}

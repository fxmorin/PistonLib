package ca.fxco.pistonlib.api.pistonLogic.families;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import ca.fxco.pistonlib.api.pistonLogic.structure.StructureGroup;
import ca.fxco.pistonlib.base.ModBlockEntities;
import ca.fxco.pistonlib.base.ModPistonFamilies;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PistonFamily {

    @Delegate(types = PistonBehavior.class, excludes = PistonBehavior.PistonBehaviorBuilder.class)
    private final PistonBehavior behavior;

    protected Map<PistonType, Block> bases = new EnumMap<>(PistonType.class);
    @Getter
    protected @Nullable Block arm;
    @Getter
    protected Block head;
    @Getter
    protected Block moving;
    @Getter
    protected BlockEntityType<? extends PistonMovingBlockEntity> movingBlockEntityType;
    @Getter
    protected ModBlockEntities.Factory<? extends PistonMovingBlockEntity> movingBlockEntityFactory;

    @Override
    public String toString() {
        return "PistonFamily{" + PistonFamilies.getId(this) + "}";
    }


    // ========================= TODO: remove setters===========================

    public void setBase(PistonType type, Block base) {
        if (ModPistonFamilies.requireNotLocked()) {
            this.bases.put(type, base);
        }
    }

    public void setArm(Block arm) {
        if (ModPistonFamilies.requireNotLocked()) {
            this.arm = arm;
        }
    }

    public void setHead(Block head) {
        if (ModPistonFamilies.requireNotLocked()) {
            this.head = head;
        }
    }

    public void setMoving(Block moving) {
        if (ModPistonFamilies.requireNotLocked()) {
            this.moving = moving;
        }
    }

    public void setMovingBlockEntity(
        BlockEntityType<? extends PistonMovingBlockEntity> type,
        ModBlockEntities.Factory<? extends PistonMovingBlockEntity> factory
    ) {
        if (ModPistonFamilies.requireNotLocked()) {
            this.movingBlockEntityType = type;
            this.movingBlockEntityFactory = factory;
        }
    }

    // =========================================================================

    public boolean hasCustomTextures() {
        return false;
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
}

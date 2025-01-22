package ca.fxco.pistonlib.pistonLogic.controller;

import ca.fxco.api.pistonlib.pistonLogic.PistonEvents;
import ca.fxco.api.pistonlib.pistonLogic.PistonMoveBehavior;
import ca.fxco.api.pistonlib.pistonLogic.controller.PistonController;
import ca.fxco.api.pistonlib.pistonLogic.structure.StructureRunner;
import ca.fxco.api.pistonlib.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.PistonLibConfig;
import ca.fxco.pistonlib.base.ModTags;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonArmBlock;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonHeadBlock;
import ca.fxco.pistonlib.helpers.PistonLibBehaviorManager;
import ca.fxco.pistonlib.helpers.Utils;
import ca.fxco.pistonlib.pistonLogic.structureResolvers.BasicStructureResolver;
import ca.fxco.pistonlib.pistonLogic.structureResolvers.MergingPistonStructureResolver;
import ca.fxco.pistonlib.pistonLogic.structureRunners.BasicStructureRunner;
import ca.fxco.pistonlib.pistonLogic.structureRunners.MergingStructureRunner;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;

import static net.minecraft.world.level.block.Block.*;
import static net.minecraft.world.level.block.piston.PistonBaseBlock.EXTENDED;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

/**
 * The base piston controller that mimics vanilla pistons.
 *
 * @author FX
 * @since 1.0.4
 */
@RequiredArgsConstructor
public class VanillaPistonController implements PistonController {

    private final PistonFamily family;
    private final PistonType type;

    @Override
    public PistonFamily getFamily() {
        return family;
    }

    @Override
    public PistonType getType() {
        return type;
    }

    @Override
    public BasicStructureResolver newStructureResolver(Level level, BlockPos pos, Direction facing,
                                                       int length, boolean extend) {
        return PistonLibConfig.mergingApi ?
                new MergingPistonStructureResolver(this, level, pos, facing, length, extend) :
                new BasicStructureResolver(this, level, pos, facing, length, extend);
    }

    @Override
    public StructureRunner newStructureRunner(
            Level level, BlockPos pos, Direction facing, int length, boolean extend,
            BasicStructureResolver.Factory<? extends BasicStructureResolver> structureProvider
    ) {
        PistonFamily family = getFamily();
        PistonType type = getType();
        return PistonLibConfig.mergingApi ?
                new MergingStructureRunner(level, pos, facing, length, family, type, extend , structureProvider) :
                new BasicStructureRunner(level, pos, facing, length, family, type, extend , structureProvider);
    }

    @Override
    public int getLength(Level level, BlockPos pos, BlockState state) {
        return state.getValue(EXTENDED) ? getFamily().getMaxLength() : getFamily().getMinLength();
    }

    @Override
    public boolean hasNeighborSignal(Level level, BlockPos pos, Direction facing) {
        return Utils.hasNeighborSignalExceptFromFacing(level, pos, facing) ||
                level.pl$hasQuasiNeighborSignal(pos, 1);
    }

    @Override
    public void checkIfExtend(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        Direction facing = state.getValue(FACING);
        int length = this.getLength(level, pos, state);
        boolean shouldExtend = hasNeighborSignal(level, pos, facing);

        PistonFamily family = getFamily();
        if (PistonLibConfig.headlessPistonFix && length > family.getMinLength()) {
            BlockState blockState = level.getBlockState(pos.relative(facing, length));
            if (shouldExtend && !blockState.is(family.getMoving()) && !blockState.is(family.getHead())) {
                level.removeBlock(pos, false);
                ItemEntity itemEntity = new ItemEntity(
                        level,
                        pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(family.getBase(getType()).asItem())
                );
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
                return;
            }
        }

        if (shouldExtend && length < family.getMaxLength()) {
            if (this.newStructureResolver(level, pos, facing, length, true).resolve()) {
                level.blockEvent(pos, state.getBlock(), PistonEvents.EXTEND, facing.get3DDataValue());
            }
        } else if (!shouldExtend && length > family.getMinLength()) {
            int type = getRetractType((ServerLevel)level, pos, facing, length);
            if (type != PistonEvents.NONE) {
                level.blockEvent(pos, state.getBlock(), type, facing.get3DDataValue());
            }
        }
    }

    @Override
    public int getRetractType(ServerLevel level, BlockPos pos, Direction facing, int length) {
        // make sure the piston doesn't try to retract while it's already retracting
        BlockPos headPos = pos.relative(facing, length);
        BlockState headState = level.getBlockState(headPos);

        PistonFamily family = getFamily();
        if (headState.is(family.getMoving())) {
            if (level.getBlockEntity(headPos) instanceof PistonMovingBlockEntity mbe &&
                    mbe.isSourcePiston() && !mbe.isExtending() && mbe.getDirection() == facing) {
                return PistonEvents.NONE;
            }
        }

        BlockPos frontPos = pos.relative(facing, length + 1);
        BlockState frontState = level.getBlockState(frontPos);

        if (frontState.is(family.getMoving()) && frontState.getValue(FACING) == facing) {
            if (level.getBlockEntity(frontPos) instanceof PistonMovingBlockEntity mbe &&
                    mbe.isExtending() &&
                    (mbe.getProgress(0.0F) < 0.5F ||
                            mbe.getLastTicked() == level.getGameTime() || level.isHandlingTick())) {
                return PistonEvents.RETRACT_NO_PULL;
            }
        }

        return PistonEvents.RETRACT;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int type, int data) {
        Direction facing = state.getValue(FACING);

        if (!level.isClientSide()) {
            boolean shouldExtend = this.hasNeighborSignal(level, pos, facing);

            if (shouldExtend && PistonEvents.isRetract(type)) {
                level.setBlock(pos, state.setValue(EXTENDED, true), UPDATE_CLIENTS);
                return false;
            }
            if (!shouldExtend && PistonEvents.isExtend(type)) {
                return false;
            }
        }

        int length = this.getLength(level, pos, state);

        if (PistonEvents.isExtend(type)) {
            if (!this.moveBlocks(level, pos, facing, length, true)) {
                return false;
            }

            if (length > 0) {
                BlockPos armPos = pos.relative(facing, length);
                BlockState armState = getFamily().getArm().defaultBlockState().
                        setValue(BasicPistonArmBlock.FACING, facing).
                        setValue(BasicPistonArmBlock.SHORT, false);

                level.setBlock(armPos, armState, UPDATE_MOVE_BY_PISTON | UPDATE_ALL);
            } else {
                level.setBlock(pos, state.setValue(EXTENDED, true), UPDATE_MOVE_BY_PISTON | UPDATE_ALL);
            }

            playEvents(level, GameEvent.BLOCK_ACTIVATE, pos);
        } else if (PistonEvents.isRetract(type)) {
            BlockPos headPos = pos.relative(facing, length);
            BlockEntity headBlockEntity = level.getBlockEntity(headPos);

            if (headBlockEntity instanceof BasicMovingBlockEntity mbe) {
                mbe.finalTick();
            }

            PistonFamily family = getFamily();
            PistonType pistonType = getType();
            int newLength = length - 1;
            BlockPos sourcePos = pos.relative(facing, newLength);
            BlockState sourceState = (newLength > 0)
                    ? family.getHead().defaultBlockState()
                    .setValue(BasicPistonHeadBlock.FACING, Direction.from3DDataValue(data & 7))
                    .setValue(BasicPistonHeadBlock.TYPE, pistonType)
                    : state.getBlock().defaultBlockState()
                    .setValue(FACING, Direction.from3DDataValue(data & 7));

            BlockState movingBaseState = family.getMoving().defaultBlockState()
                    .setValue(MovingPistonBlock.FACING, facing)
                    .setValue(MovingPistonBlock.TYPE, pistonType);
            BlockEntity movingBaseBlockEntity = family.newMovingBlockEntity(
                    sourcePos,
                    movingBaseState,
                    sourceState,
                    null,
                    facing,
                    false,
                    true
            );
            level.setBlock(sourcePos, movingBaseState, UPDATE_MOVE_BY_PISTON | UPDATE_KNOWN_SHAPE | UPDATE_INVISIBLE);
            level.setBlockEntity(movingBaseBlockEntity);

            level.updateNeighborsAt(sourcePos, movingBaseState.getBlock());
            movingBaseState.updateNeighbourShapes(level, sourcePos, UPDATE_CLIENTS);

            if (pistonType == PistonType.STICKY) {
                boolean droppedBlock = false;

                BlockPos frontPos = pos.relative(facing, length + 1);
                BlockState frontState = level.getBlockState(frontPos);

                if (frontState.is(family.getMoving())) {
                    BlockEntity frontBlockEntity = level.getBlockEntity(frontPos);

                    if (frontBlockEntity instanceof PistonMovingBlockEntity mbe &&
                            mbe.getDirection() == facing && mbe.isExtending()) {
                        mbe.finalTick();
                        droppedBlock = true;
                    }
                }
                if (!droppedBlock) {
                    if (type == PistonEvents.RETRACT_NO_PULL || frontState.isAir() ||
                            (frontState.getPistonPushReaction() != PushReaction.NORMAL && !frontState.is(ModTags.PISTONS)) ||
                            !canMoveBlock(frontState, level, frontPos, facing.getOpposite(), false, facing)) {
                        if (!PistonLibConfig.illegalBreakingFix ||
                                level.getBlockState(headPos).getDestroySpeed(level, headPos) != -1.0F) {
                            level.removeBlock(headPos, false);
                        }
                    } else {
                        this.moveBlocks(level, pos, facing, length, false);
                    }
                }
            } else {
                if (!PistonLibConfig.illegalBreakingFix ||
                        level.getBlockState(headPos).getDestroySpeed(level, headPos) != -1.0F) {
                    level.removeBlock(headPos, false);
                }
            }

            playEvents(level, GameEvent.BLOCK_DEACTIVATE, pos);
        }

        return true;
    }

    @Override
    public void playEvents(Level level, GameEvent event, BlockPos pos) {
        level.playSound(
                null,
                pos,
                event == GameEvent.BLOCK_DEACTIVATE ?
                        SoundEvents.PISTON_CONTRACT : SoundEvents.PISTON_EXTEND,
                SoundSource.BLOCKS,
                0.5F,
                0.6F + 0.25F * level.getRandom().nextFloat()
        );
        level.gameEvent(null, event, pos);
    }

    @Override
    public boolean canMoveBlock(BlockState state, Level level, BlockPos pos, Direction moveDir,
                                boolean allowDestroy, Direction pistonFacing) {
        // coordinate related checks (world height/world border)

        if (level.isOutsideBuildHeight(pos) || !(PistonLibConfig.pushThroughWorldBorderFix ?
                level.getWorldBorder().isWithinBounds(pos.relative(moveDir)) :
                level.getWorldBorder().isWithinBounds(pos))) {
            return false;
        } else if (state.isAir()) {
            return true; // air is never in the way
        } else if (moveDir == Direction.DOWN) {
            if (pos.getY() == level.getMinBuildHeight()) {
                return false;
            }
        } else if (moveDir == Direction.UP && pos.getY() == level.getMaxBuildHeight() - 1) {
            return false;
        }


        // piston push reaction/ custom piston behavior

        if (state.pl$usesConfigurablePistonBehavior()) { // This is where stuff gets fun
            if (!state.pl$isMovable(level, pos)) {
                return false;
            } else if (moveDir == pistonFacing) {
                if (!state.pl$canPistonPush(level, pos, moveDir)) {
                    return false;
                }
            } else {
                if (!state.pl$canPistonPull(level, pos, moveDir)) {
                    return false;
                }
            }
            if (state.pl$canDestroy(level, pos) && !allowDestroy) {
                return false;
            }
        } else {
            if (state.is(ModTags.UNPUSHABLE)) {
                if (PistonLibConfig.behaviorOverrideApi) {
                    PistonMoveBehavior override = PistonLibBehaviorManager.getOverride(state);
                    if (!override.isPresent()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (state.is(ModTags.PISTONS)) {
                if (PistonLibConfig.behaviorOverrideApi) {
                    PistonMoveBehavior override = PistonLibBehaviorManager.getOverride(state);
                    if (!override.isPresent()) {
                        return !state.getValue(EXTENDED) && canMoveBlock(state);
                    }
                } else {
                    return !state.getValue(EXTENDED) && canMoveBlock(state);
                }
            } else if (state.getDestroySpeed(level, pos) == -1.0F) {
                if (PistonLibConfig.behaviorOverrideApi) {
                    PistonMoveBehavior override = PistonLibBehaviorManager.getOverride(state);
                    if (!override.isPresent()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            // Pistons shouldn't be checked against destroy speed or PistonPushReaction, unless using custom override
            switch (state.getPistonPushReaction()) {
                case BLOCK -> { return false; }
                case DESTROY -> {
                    if (!allowDestroy) {
                        return false;
                    }
                }
                case PUSH_ONLY -> {
                    if (moveDir != pistonFacing) {
                        return false;
                    }
                }
                default -> { }
            }
        }

        // custom piston behavior
        return canMoveBlock(state);
    }

    @Override
    public boolean canMoveBlock(BlockState state) {
        return !state.hasBlockEntity();
    }

    @Override
    public boolean moveBlocks(Level level, BlockPos pos, Direction facing, int length, boolean extend) {
        return newStructureRunner(level, pos, facing, length, extend, this::newStructureResolver).run();
    }
}

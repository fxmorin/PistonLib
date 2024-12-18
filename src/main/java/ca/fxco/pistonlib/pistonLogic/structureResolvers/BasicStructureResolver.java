package ca.fxco.pistonlib.pistonLogic.structureResolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.fxco.pistonlib.PistonLibConfig;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import ca.fxco.pistonlib.pistonLogic.accessible.ConfigurablePistonBehavior;
import ca.fxco.pistonlib.pistonLogic.internal.BlockStateBaseExpandedSticky;
import ca.fxco.pistonlib.pistonLogic.sticky.StickRules;
import ca.fxco.pistonlib.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.pistonLogic.sticky.StickyType;

import ca.fxco.pistonlib.pistonLogic.internal.BlockStateBasePushReaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class BasicStructureResolver extends PistonStructureResolver {

    protected final BasicPistonBaseBlock piston;
    protected final int maxMovableWeight;
    protected final int length;
    protected int movingWeight = 0; // used instead of the toPush size so some blocks can be harder to push than others

    public BasicStructureResolver(BasicPistonBaseBlock piston, Level level, BlockPos pos,
                                  Direction facing, int length, boolean extend) {
        super(level, pos, facing, extend);

        this.piston = piston;
        this.length = length;
        this.maxMovableWeight = piston.getFamily().getPushLimit();

        this.startPos = this.pistonPos.relative(this.pistonDirection, this.length + 1);
    }

    protected void resetResolver() {
        this.toPush.clear();
        this.toDestroy.clear();
        this.movingWeight = 0;
    }

    @Override
    public boolean resolve() {
        resetResolver();

        if (this.piston.getFamily().hasCustomLength() && !resolveLongPiston()) {
            return false;
        }

        return runStructureGeneration();
    }

    protected boolean resolveLongPiston() {
        // make sure the piston doesn't try to extend while it's already extending
        // or retract while it's already retracting
        BlockPos headPos = this.pistonPos.relative(this.pistonDirection, this.length);
        BlockState headState = this.level.getBlockState(headPos);
        if (headState.is(this.piston.getFamily().getMoving())) {
            BlockEntity blockEntity = this.level.getBlockEntity(headPos);
            if (blockEntity == null) {
                return false;
            }
            if (blockEntity.getType() == this.piston.getFamily().getMovingBlockEntityType()) {
                BasicMovingBlockEntity mbe = (BasicMovingBlockEntity)blockEntity;
                if (mbe.isSourcePiston && mbe.extending == this.extending && mbe.direction == this.pistonDirection) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean runStructureGeneration() {
        // Structure Generation
        BlockState state = this.level.getBlockState(this.startPos);
        if (!this.piston.canMoveBlock(state, this.level, this.startPos, this.pushDirection, false, this.pistonDirection)) {
            // Block directly in front is immovable, can only be true if extending, and it can be destroyed
            if (this.extending) {
                ConfigurablePistonBehavior pistonBehavior = (ConfigurablePistonBehavior)state.getBlock();
                if (pistonBehavior.usesConfigurablePistonBehavior()) {
                    if (pistonBehavior.canDestroy(this.level, this.startPos, state)) {
                        this.toDestroy.add(this.startPos);
                        return true;
                    }
                } else if (state.getPistonPushReaction() == PushReaction.DESTROY) {
                    this.toDestroy.add(this.startPos);
                    return true;
                }
            }
            return false;
        } else { // Start block is movable, now check if it's possible to move the rest, while generating the structure
            if (this.cantMove(this.startPos, !this.extending ? this.pushDirection.getOpposite() : this.pushDirection)) {
                return false;
            }
        }

        // This loops through the blocks to push and creates the branches
        for (int i = 0; i < this.toPush.size(); i++) {
            BlockPos blockPos = this.toPush.get(i);
            if (!attemptMove(this.level.getBlockState(blockPos), blockPos)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isPiston(BlockPos pos) {
        for (int i = 0; i <= this.length; i++)
            if (this.pistonPos.relative(this.pistonDirection, i).equals(pos))
                return true;
    	return false;
    }

    protected boolean cantMoveAdjacentBlocks(BlockPos pos) {
        BlockState blockState = this.level.getBlockState(pos);
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != this.pushDirection.getAxis()) {
                BlockPos blockPos = pos.relative(direction);
                BlockState blockState2 = this.level.getBlockState(blockPos);
                if (canAdjacentBlockStick(direction, blockState, blockState2) && this.cantMove(blockPos, direction)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean cantMoveAdjacentStickyBlocks(Map<Direction, StickyType> sides, BlockPos pos) {
        BlockState blockState = this.level.getBlockState(pos);
        for (Map.Entry<Direction, StickyType> sideData : sides.entrySet()) {
            StickyType stickyType = sideData.getValue();
            if (stickyType == StickyType.NO_STICK) {
                continue;
            }
            Direction dir = sideData.getKey();
            if (dir.getAxis() != this.pushDirection.getAxis()) {
                BlockPos blockPos = pos.relative(dir);
                BlockState adjState = this.level.getBlockState(blockPos);
                if (stickyType == StickyType.CONDITIONAL) {
                    if (stickyType.canStick(blockState, adjState, dir) && this.cantMove(blockPos, dir)) {
                        return true;
                    }
                    continue;
                }
                if (canAdjacentBlockStick(dir, blockState, adjState) && this.cantMove(blockPos, dir)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Stickiness checks
    protected static boolean canAdjacentBlockStick(Direction dir, BlockState state, BlockState adjState) {
        return canAdjacentBlockStick(dir, state, adjState, true);
    }

    // Stickiness checks
    protected static boolean canAdjacentBlockStick(Direction dir, BlockState state, BlockState adjState, boolean attemptIndirect) {
        BlockStateBaseExpandedSticky adjStick = (BlockStateBaseExpandedSticky)adjState;
        if (adjStick.usesConfigurablePistonStickiness()) {
            if (!adjStick.isSticky()) {
                return attemptIndirect && canIndirectBlockStick(dir, state, adjState);
            }
            StickyType type = adjStick.sideStickiness(dir.getOpposite());
            if (type == StickyType.CONDITIONAL) {
                if (type.canStick(state, adjState, dir)) {
                    return true;
                }
                return attemptIndirect && canIndirectBlockStick(dir, state, adjState);
            }
            return type != StickyType.NO_STICK; // If NO_STICKY, even indirect sticky cannot stick to this side!
        }
        StickyGroup stickyGroup1 = ((BlockStateBaseExpandedSticky)state).getStickyGroup();
        if (stickyGroup1 != null) {
            return StickRules.test(stickyGroup1, adjStick.getStickyGroup());
        }
        return attemptIndirect && canIndirectBlockStick(dir, state, adjState);
    }

    protected static boolean canIndirectBlockStick(Direction dir, BlockState state, BlockState adjState) {
        if (PistonLibConfig.indirectStickyApi) {
            if (PistonLibConfig.allStickyTypesAreIndirect) {
                return canAdjacentBlockStick(dir.getOpposite(), adjState, state, false);
            }
            BlockStateBaseExpandedSticky stick = (BlockStateBaseExpandedSticky)state;
            if (stick.usesConfigurablePistonStickiness() && stick.isSticky()) {
                StickyType type = stick.sideStickiness(dir);
                return type.ordinal() >= StickyType.STRONG.ordinal();
            }
        }
        return false;
    }

    protected boolean attemptMove(BlockState state, BlockPos pos) {
        BlockStateBaseExpandedSticky sticky = (BlockStateBaseExpandedSticky)state;
        if (PistonLibConfig.indirectStickyApi) {
            if (sticky.usesConfigurablePistonStickiness()) {
                return !cantMoveAdjacentStickyBlocks(sticky.stickySides(), pos);
            }
            return !cantMoveAdjacentBlocks(pos);
        }
        if (sticky.usesConfigurablePistonStickiness()) {
            return !sticky.isSticky() || !cantMoveAdjacentStickyBlocks(sticky.stickySides(), pos);
        }
        return !sticky.hasStickyGroup() || !cantMoveAdjacentBlocks(pos);
    }

    protected boolean isSticky(BlockState state, BlockState adjState, Direction dir) {
        BlockStateBaseExpandedSticky sticky = (BlockStateBaseExpandedSticky)state;
        if (PistonLibConfig.indirectStickyApi) {
            // Some conditional blocks such as double blocks, shouldn't propagate indirect sticky conditions
            if (sticky.usesConfigurablePistonStickiness() && sticky.isSticky()) {
                StickyType type = sticky.sideStickiness(dir);
                if (type == StickyType.CONDITIONAL) {
                    return type.canStick(state, adjState, dir);
                }
                return sticky.propagatesIndirectSticky();
            }
            // All other blocks should check the blocks around them for indirect sticky blocks, except air
            return !state.isAir();
        }
        if (sticky.usesConfigurablePistonStickiness()) {
            if (sticky.isSticky()) {
                StickyType type = sticky.sideStickiness(dir);
                if (type == StickyType.CONDITIONAL) {
                    return type.canStick(state, adjState, dir);
                }
                return type.ordinal() >= StickyType.STICKY.ordinal();
            }
            return false;
        }
        return sticky.hasStickyGroup();
    }

    protected boolean cantMove(BlockPos pos, Direction dir) {
        BlockState state = this.level.getBlockState(pos);
        if (state.isAir() || isPiston(pos) || this.toPush.contains(pos)) {
            return false;
        }
        if (!this.piston.canMoveBlock(state, this.level, pos, this.pushDirection, false, dir)) {
            return false;
        }
        int weight = ((BlockStateBasePushReaction)state).getWeight();
        if (weight + this.movingWeight > this.maxMovableWeight) {
            return true;
        }
        int i = 1;
        Direction pullDirection = this.pushDirection.getOpposite();
        BlockState currentBlockState = state;
        BlockPos nextPos = pos.relative(pullDirection, i);
        BlockState nextState = this.level.getBlockState(nextPos);
        while (isSticky(currentBlockState, nextState, pullDirection)) {
            if (nextState.isAir() ||
                    !canAdjacentBlockStick(pullDirection, currentBlockState, nextState) ||
                    isPiston(nextPos) ||
                    !this.piston.canMoveBlock(nextState, this.level, nextPos,
                            this.pushDirection, false, pullDirection)) {
                break;
            }
            weight += ((BlockStateBasePushReaction)nextState).getWeight();
            if (weight + this.movingWeight > this.maxMovableWeight) {
                return true;
            }
            ++i;
            nextPos = pos.relative(pullDirection, i);
            currentBlockState = nextState;
            nextState = this.level.getBlockState(nextPos);
        }
        this.movingWeight += weight;
        int j = 0, k;
        for (k = i - 1; k >= 0; --k) {
            this.toPush.add(pos.relative(pullDirection, k));
            ++j;
        }
        k = 1;
        while(true) {
            BlockPos pos2 = pos.relative(this.pushDirection, k);
            int l = this.toPush.indexOf(pos2);
            if (l > -1) {
                this.setMovedBlocks(j, l);
                for (int m = 0; m <= l + j; ++m) {
                    BlockPos pos3 = this.toPush.get(m);
                    state = this.level.getBlockState(pos3);
                    if (!attemptMove(state, pos3)) {
                        return true;
                    }
                }
                return false;
            }
            state = this.level.getBlockState(pos2);
            if (state.isAir()) {
                return false;
            } else if (isPiston(pos2)) {
                return true;
            } else if (!piston.canMoveBlock(state, this.level, pos2, this.pushDirection, true, this.pushDirection)) {
                return true;
            }
            ConfigurablePistonBehavior pistonBehavior = (ConfigurablePistonBehavior)state.getBlock();
            if (pistonBehavior.usesConfigurablePistonBehavior()) {
                if (pistonBehavior.canDestroy(this.level, pos2, state)) {
                    this.toDestroy.add(pos2);
                    return false;
                }
            } else if (state.getPistonPushReaction() == PushReaction.DESTROY) {
                this.toDestroy.add(pos2);
                return false;
            }
            weight = pistonBehavior.getWeight(state);
            if (weight + this.movingWeight > this.maxMovableWeight) {
                return true;
            }
            this.movingWeight += weight;
            this.toPush.add(pos2);
            ++j;
            ++k;
        }
    }

    protected void setMovedBlocks(int from, int to) {
        List<BlockPos> list = new ArrayList<>();
        List<BlockPos> list2 = new ArrayList<>();
        List<BlockPos> list3 = new ArrayList<>();
        list.addAll(this.toPush.subList(0, to));
        list2.addAll(this.toPush.subList(this.toPush.size() - from, this.toPush.size()));
        list3.addAll(this.toPush.subList(to, this.toPush.size() - from));
        this.toPush.clear();
        this.toPush.addAll(list);
        this.toPush.addAll(list2);
        this.toPush.addAll(list3);
    }

    public int getMoveLimit() {
        return this.maxMovableWeight;
    }

    @FunctionalInterface
    public interface Factory<T extends BasicStructureResolver> {

        T create(Level level, BlockPos pos, Direction facing, int length, boolean extend);

    }
}

package ca.fxco.pistonlib.pistonLogic.structureResolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.fxco.api.pistonlib.pistonLogic.controller.PistonController;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickRules;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroup;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyType;
import ca.fxco.pistonlib.PistonLibConfig;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class BasicStructureResolver extends PistonStructureResolver {

    protected final PistonController controller;
    protected final int maxMovableWeight;
    protected final int length;
    protected int movingWeight = 0; // used instead of the toPush size so some blocks can be harder to push than others

    public BasicStructureResolver(PistonController controller, Level level, BlockPos pos,
                                  Direction facing, int length, boolean extend) {
        super(level, pos, facing, extend);

        this.controller = controller;
        this.length = length;
        this.maxMovableWeight = controller.getFamily().getPushLimit();

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

        if (this.controller.getFamily().hasCustomLength() && !resolveLongPiston()) {
            return false;
        }

        return runStructureGeneration();
    }

    protected boolean resolveLongPiston() {
        // make sure the piston doesn't try to extend while it's already extending
        // or retract while it's already retracting
        BlockPos headPos = this.pistonPos.relative(this.pistonDirection, this.length);
        BlockState headState = this.level.getBlockState(headPos);
        if (headState.is(this.controller.getFamily().getMoving())) {
            BlockEntity blockEntity = this.level.getBlockEntity(headPos);
            if (blockEntity == null) {
                return false;
            }
            if (blockEntity.getType() == this.controller.getFamily().getMovingBlockEntityType()) {
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
        if (!this.controller.canMoveBlock(state, this.level, this.startPos, this.pushDirection, false, this.pistonDirection)) {
            // Block directly int front is immovable, can only be true if extending, and it can be destroyed
            if (this.extending) {
                if (state.pl$usesConfigurablePistonBehavior()) {
                    if (state.pl$canDestroy(this.level, this.startPos)) {
                        this.toDestroy.add(this.startPos);
                        return true;
                    }
                } else if (state.getPistonPushReaction() == PushReaction.DESTROY) {
                    this.toDestroy.add(this.startPos);
                    return true;
                }
                return false;
            }
            return false;
        } else { // Start block is not immovable, we can check if its possible to move. Also generates structure
            if (this.cantMove(this.startPos, !this.extending ? this.pushDirection.getOpposite() : this.pushDirection)) {
                return false;
            }
        }

        // This loops through the blocks to push and creates the branches
        for (int i = 0; i < this.toPush.size(); ++i) {
            BlockPos blockPos = this.toPush.get(i);
            state = this.level.getBlockState(blockPos);
            if (!attemptMove(state, blockPos)) {
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
                if (stickyType == StickyType.CONDITIONAL && !stickyType.canStick(blockState, adjState, dir)) {
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
        if (adjState.pl$usesConfigurablePistonStickiness()) {
            if (!adjState.pl$isSticky()) {
                return attemptIndirect && canIndirectBlockStick(dir, state, adjState);
            }
            StickyType type = adjState.pl$sideStickiness(dir.getOpposite());
            if (type == StickyType.CONDITIONAL && !type.canStick(state, adjState, dir)) {
                return true;
            }
            return type != StickyType.NO_STICK; // If NO_STICKY, even indirect sticky cannot stick to this side!
        }
        StickyGroup stickyGroup1 = state.pl$getStickyGroup();
        if (stickyGroup1 != null) {
            return StickRules.test(stickyGroup1, adjState.pl$getStickyGroup());
        }
        return attemptIndirect && canIndirectBlockStick(dir, state, adjState);
    }

    protected static boolean canIndirectBlockStick(Direction dir, BlockState state, BlockState adjState) {
        if (PistonLibConfig.indirectStickyApi) {
            if (PistonLibConfig.allStickyTypesAreIndirect) {
                return canAdjacentBlockStick(dir.getOpposite(), adjState, state, false);
            }
            if (state.pl$usesConfigurablePistonStickiness()) {
                if (!state.pl$isSticky()) {
                    return false;
                }
                StickyType type = state.pl$sideStickiness(dir);
                return type.ordinal() >= StickyType.STRONG.ordinal();
            }
        }
        return false;
    }

    protected boolean attemptMove(BlockState state, BlockPos pos) {
        if (PistonLibConfig.indirectStickyApi) {
            if (state.pl$usesConfigurablePistonStickiness()) {
                return !cantMoveAdjacentStickyBlocks(state.pl$stickySides(), pos);
            }
            return !cantMoveAdjacentBlocks(pos);
        }
        if (state.pl$usesConfigurablePistonStickiness()) {
            return !state.pl$isSticky() || !cantMoveAdjacentStickyBlocks(state.pl$stickySides(), pos);
        }
        return !state.pl$hasStickyGroup() || !cantMoveAdjacentBlocks(pos);
    }

    protected static boolean isSticky(BlockState state, Direction dir) {
        if (PistonLibConfig.indirectStickyApi) {
            // Basically all blocks should check the blocks around them for indirect sticky blocks, except air
            return !state.isAir();
        }
        if (state.pl$usesConfigurablePistonStickiness()) {
            return state.pl$isSticky() && state.pl$sideStickiness(dir).ordinal() >= StickyType.STICKY.ordinal();
        }
        return state.pl$hasStickyGroup();
    }

    protected boolean cantMove(BlockPos pos, Direction dir) {
        BlockState state = this.level.getBlockState(pos);
        if (state.isAir() || isPiston(pos) || this.toPush.contains(pos)) return false;
        if (!this.controller.canMoveBlock(state, this.level, pos, this.pushDirection, false, dir)) return false;
        int weight = state.pl$getWeight();
        if (weight + this.movingWeight > this.maxMovableWeight) {
            return true;
        }
        int i = 1;
        Direction dir2 = this.pushDirection.getOpposite();
        while (isSticky(state, dir2)) {
            BlockPos blockPos = pos.relative(dir2, i);
            BlockState blockState2 = state;
            state = this.level.getBlockState(blockPos);
            if (state.isAir() ||
                    !canAdjacentBlockStick(dir2, blockState2, state) ||
                    isPiston(blockPos) ||
                    !this.controller.canMoveBlock(state, this.level, blockPos, this.pushDirection, false, dir2)) {
                break;
            }
            weight += state.pl$getWeight();
            if (weight + this.movingWeight > this.maxMovableWeight) {
                return true;
            }
            ++i;
        }
        this.movingWeight += weight;
        int j = 0, k;
        for(k = i - 1; k >= 0; --k) {
            this.toPush.add(pos.relative(dir2, k));
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
            } else if (!controller.canMoveBlock(state, this.level, pos2, this.pushDirection, true, this.pushDirection)) {
                return true;
            }
            if (state.pl$usesConfigurablePistonBehavior()) {
                if (state.pl$canDestroy(this.level, pos2)) {
                    this.toDestroy.add(pos2);
                    return false;
                }
            } else if (state.getPistonPushReaction() == PushReaction.DESTROY) {
                this.toDestroy.add(pos2);
                return false;
            }
            weight = state.pl$getWeight();
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
        List<BlockPos> list = new ArrayList<>(this.toPush.subList(0, to));
        List<BlockPos> list2 = new ArrayList<>(this.toPush.subList(this.toPush.size() - from, this.toPush.size()));
        List<BlockPos> list3 = new ArrayList<>(this.toPush.subList(to, this.toPush.size() - from));
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

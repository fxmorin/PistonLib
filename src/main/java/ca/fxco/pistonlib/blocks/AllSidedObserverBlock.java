package ca.fxco.pistonlib.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Redstone;

import static ca.fxco.pistonlib.PistonLib.DIRECTIONS;

public class AllSidedObserverBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public AllSidedObserverBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, state = state.cycle(POWERED), UPDATE_CLIENTS);
        if (state.getValue(POWERED)) {
            level.scheduleTick(pos, this, 2);
        }
        this.updateNeighbors(level, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess,
                                  BlockPos pos, Direction dir, BlockPos neighborPos,
                                  BlockState neighborState, RandomSource random) {
        if (!state.getValue(POWERED) && shouldNeighborTrigger(neighborState)) {
            this.startSignal(level, scheduledTickAccess, pos);
        }

        return super.updateShape(state, level, scheduledTickAccess, pos, dir, neighborPos, neighborState, random);
    }

    private boolean shouldNeighborTrigger(BlockState neighborState) {
        if (neighborState.hasProperty(BlockStateProperties.POWERED)) {
            return neighborState.getValue(BlockStateProperties.POWERED);
        } else if (neighborState.hasProperty(BlockStateProperties.POWER)) {
            return neighborState.getValue(BlockStateProperties.POWER) != Redstone.SIGNAL_NONE;
        } else if (neighborState.hasProperty(BlockStateProperties.LIT)) {
            return neighborState.getValue(BlockStateProperties.LIT);
        }
        return true;
    }

    private void startSignal(LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos) {
        if (!level.isClientSide() && !scheduledTickAccess.getBlockTicks().hasScheduledTick(pos, this)) {
            scheduledTickAccess.scheduleTick(pos, this, 2);
        }
    }

    protected void updateNeighbors(Level world, BlockPos pos) {
        for (Direction dir : DIRECTIONS) {
            BlockPos side = pos.relative(dir);

            world.neighborChanged(side, this, null);
            world.updateNeighborsAtExceptFromFacing(side, this, dir, null);
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return state.getSignal(level, pos, dir);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return state.getValue(POWERED) ? Redstone.SIGNAL_MAX : Redstone.SIGNAL_NONE;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide() && !oldState.is(this) && state.getValue(POWERED) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.setBlock(pos, state.setValue(POWERED, false), UPDATE_CLIENTS | UPDATE_KNOWN_SHAPE);
            this.updateNeighbors(level, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide() && !newState.is(this) && state.getValue(POWERED) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            this.updateNeighbors(level, pos);
        }
    }
}

package ca.fxco.pistonlib.blocks.pistons.slipperyPiston;

import ca.fxco.pistonlib.api.pistonLogic.controller.PistonController;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import ca.fxco.pistonlib.blocks.slipperyBlocks.BaseSlipperyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static ca.fxco.pistonlib.blocks.slipperyBlocks.BaseSlipperyBlock.MAX_DISTANCE;
import static ca.fxco.pistonlib.blocks.slipperyBlocks.BaseSlipperyBlock.SLIPPERY_DELAY;

public class SlipperyPistonBaseBlock extends BasicPistonBaseBlock {

    public SlipperyPistonBaseBlock(PistonController pistonController, Properties properties) {
        super(pistonController, properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(state.getBlock()) && !level.isClientSide() && level.getBlockEntity(pos) == null) {
            this.pl$getPistonController().checkIfExtend(level, pos, state, true);
            level.scheduleTick(pos, this, SLIPPERY_DELAY);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess,
                                  BlockPos pos, Direction dir, BlockPos neighborPos,
                                  BlockState neighborState, RandomSource random) {
        if (!level.isClientSide()) {
            scheduledTickAccess.scheduleTick(pos, this, SLIPPERY_DELAY);
        }
        return super.updateShape(state, level, scheduledTickAccess, pos, dir, neighborPos, neighborState, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (BaseSlipperyBlock.calculateDistance(level, pos) >= MAX_DISTANCE) {
            FallingBlockEntity.fall(level, pos, state.setValue(BlockStateProperties.EXTENDED, false));
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return BaseSlipperyBlock.calculateDistance(level, pos) < MAX_DISTANCE;
    }
}

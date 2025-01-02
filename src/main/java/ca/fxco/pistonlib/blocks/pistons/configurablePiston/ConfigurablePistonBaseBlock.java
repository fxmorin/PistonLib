package ca.fxco.pistonlib.blocks.pistons.configurablePiston;

import ca.fxco.api.pistonlib.pistonLogic.controller.PistonController;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static ca.fxco.pistonlib.blocks.slipperyBlocks.BaseSlipperyBlock.MAX_DISTANCE;
import static ca.fxco.pistonlib.blocks.slipperyBlocks.BaseSlipperyBlock.SLIPPERY_DELAY;

public class ConfigurablePistonBaseBlock extends BasicPistonBaseBlock {

    public ConfigurablePistonBaseBlock(PistonController controller) {
        super(controller);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(this) && level.getBlockEntity(pos) == null) {
            PistonController controller = this.pl$getPistonController();
            controller.checkIfExtend(level, pos, state);
            if (controller.getFamily().isSlippery() && !level.isClientSide) {
                level.scheduleTick(pos, this, SLIPPERY_DELAY);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (this.pl$getPistonController().getFamily().isSlippery() && !level.isClientSide())
            level.scheduleTick(pos, this, SLIPPERY_DELAY);
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.pl$getPistonController().getFamily().isSlippery() &&
                BaseSlipperyBlock.calculateDistance(level, pos) >= MAX_DISTANCE)
            FallingBlockEntity.fall(level, pos, state.setValue(BlockStateProperties.EXTENDED,false));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return !this.pl$getPistonController().getFamily().isSlippery() ||
                BaseSlipperyBlock.calculateDistance(level, pos) < MAX_DISTANCE;
    }
}

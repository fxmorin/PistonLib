package ca.fxco.pistonlib.blocks.pistons.veryStickyPiston;

import java.util.Map;

import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyType;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonHeadBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class StickyPistonHeadBlock extends BasicPistonHeadBlock {

    public StickyPistonHeadBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState behindState = level.getBlockState(pos.relative(state.getValue(FACING).getOpposite()));
        return this.isFittingBase(state, behindState) || behindState.is(this.getFamily().getMoving());
    }

    @Override
    public boolean pl$usesConfigurablePistonBehavior() {
        return true; // Makes the piston head movable by bypassing vanilla checks
    }

    @Override
    public boolean pl$usesConfigurablePistonStickiness() {
        return true;
    }

    @Override
    public Map<Direction, StickyType> pl$stickySides(BlockState state) {
        return Map.of(state.getValue(FACING), StickyType.STICKY, state.getValue(FACING).getOpposite(), StickyType.STICKY);
    }

    @Override
    public StickyType pl$sideStickiness(BlockState state, Direction direction) {
        return state.getValue(FACING).getAxis() == direction.getAxis() ? StickyType.STICKY : StickyType.DEFAULT;
    }
}

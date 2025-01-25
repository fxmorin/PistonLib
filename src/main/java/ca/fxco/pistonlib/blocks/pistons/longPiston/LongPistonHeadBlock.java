package ca.fxco.pistonlib.blocks.pistons.longPiston;

import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonHeadBlock;

import net.minecraft.world.level.block.state.BlockState;

public class LongPistonHeadBlock extends BasicPistonHeadBlock {

    public LongPistonHeadBlock(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isFittingBase(BlockState headState, BlockState behindState) {
        return behindState.is(this.getFamily().getArm()) ?
                behindState.getValue(FACING) == headState.getValue(FACING) :
                super.isFittingBase(headState, behindState);
    }
}

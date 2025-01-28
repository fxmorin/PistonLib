package ca.fxco.pistonlib.blocks.slipperyBlocks;

import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.base.ModStickyGroups;
import net.minecraft.world.level.block.state.BlockState;

public class SlipperySlimeBlock extends BaseSlipperyBlock {

    public SlipperySlimeBlock(Properties settings) {
        super(settings);
    }

    @Override
    public StickyGroup pl$getStickyGroup(BlockState state) {
        return ModStickyGroups.SLIME;
    }
}

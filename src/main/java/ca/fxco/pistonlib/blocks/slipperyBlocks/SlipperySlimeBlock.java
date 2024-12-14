package ca.fxco.pistonlib.blocks.slipperyBlocks;

import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.base.ModStickyGroups;

public class SlipperySlimeBlock extends BaseSlipperyBlock {

    public SlipperySlimeBlock(Properties settings) {
        super(settings);
    }

    @Override
    public StickyGroup pl$getStickyGroup() {
        return ModStickyGroups.SLIME;
    }
}

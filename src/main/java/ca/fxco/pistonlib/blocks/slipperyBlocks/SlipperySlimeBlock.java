package ca.fxco.pistonlib.blocks.slipperyBlocks;

import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroup;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroups;

public class SlipperySlimeBlock extends BaseSlipperyBlock {

    public SlipperySlimeBlock(Properties settings) {
        super(settings);
    }

    @Override
    public StickyGroup pl$getStickyGroup() {
        return StickyGroups.SLIME;
    }
}

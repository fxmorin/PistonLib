package ca.fxco.pistonlib.blocks;

import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroups;
import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SlimyPoweredBlock extends PoweredBlock {

	public SlimyPoweredBlock(Properties properties) {
        super(properties);
    }

    @Override
    public StickyGroup pl$getStickyGroup(BlockState state) {
        return StickyGroups.SLIME;
    }
}

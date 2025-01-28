package ca.fxco.pistonlib.mixin.stickyGroup;

import ca.fxco.pistonlib.api.block.PLBlockBehaviour;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.base.ModStickyGroups;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SlimeBlock.class)
public class SlimeBlock_honeyMixin implements PLBlockBehaviour {

    @Override
    public StickyGroup pl$getStickyGroup(BlockState state) {
        return ModStickyGroups.SLIME;
    }
}

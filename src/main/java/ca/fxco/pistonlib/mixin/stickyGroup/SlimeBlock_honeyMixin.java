package ca.fxco.pistonlib.mixin.stickyGroup;

import ca.fxco.api.pistonlib.block.PLBlockBehaviour;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroup;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroups;
import net.minecraft.world.level.block.SlimeBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SlimeBlock.class)
public class SlimeBlock_honeyMixin implements PLBlockBehaviour {

    @Override
    public StickyGroup pl$getStickyGroup() {
        return StickyGroups.SLIME;
    }
}

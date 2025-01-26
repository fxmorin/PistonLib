package ca.fxco.pistonlib.mixin.stickyGroup;

import ca.fxco.pistonlib.api.block.PLBlockBehaviour;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroup;
import ca.fxco.pistonlib.base.ModStickyGroups;
import net.minecraft.world.level.block.HoneyBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HoneyBlock.class)
public class HoneyBlock_slimeMixin implements PLBlockBehaviour {

    @Override
    public StickyGroup pl$getStickyGroup() {
        return ModStickyGroups.HONEY;
    }
}

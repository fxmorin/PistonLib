package ca.fxco.pistonlib.mixin;

import ca.fxco.pistonlib.api.block.PLBlockProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockBehaviour.Properties.class)
public class BlockProperties_coreMixin implements PLBlockProperties {
}

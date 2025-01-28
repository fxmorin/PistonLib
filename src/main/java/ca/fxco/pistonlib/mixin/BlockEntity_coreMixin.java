package ca.fxco.pistonlib.mixin;

import ca.fxco.pistonlib.api.blockEntity.PLBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class BlockEntity_coreMixin implements PLBlockEntity {
}

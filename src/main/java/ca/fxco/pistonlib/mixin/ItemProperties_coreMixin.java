package ca.fxco.pistonlib.mixin;

import ca.fxco.pistonlib.api.item.PLItemProperties;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.Properties.class)
public class ItemProperties_coreMixin implements PLItemProperties {
}

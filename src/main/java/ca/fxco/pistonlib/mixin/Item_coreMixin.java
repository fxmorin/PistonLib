package ca.fxco.pistonlib.mixin;

import ca.fxco.pistonlib.api.item.PLItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class Item_coreMixin implements PLItem {
}

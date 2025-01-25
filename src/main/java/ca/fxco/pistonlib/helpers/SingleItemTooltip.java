package ca.fxco.pistonlib.helpers;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.component.BundleContents;

public record SingleItemTooltip(BundleContents contents) implements TooltipComponent {
}

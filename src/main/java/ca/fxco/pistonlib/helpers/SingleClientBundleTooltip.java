package ca.fxco.pistonlib.helpers;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.world.item.component.BundleContents;

public class SingleClientBundleTooltip extends ClientBundleTooltip {
    public SingleClientBundleTooltip(BundleContents bundleTooltip) {
        super(bundleTooltip);
    }

    @Override
    public int getContentXOffset(int k) {
        return 1;
    }

    @Override
    public int gridSizeY() {
        return 1;
    }
}

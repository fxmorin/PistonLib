package ca.fxco.pistonlib.mixin.merging;

import ca.fxco.pistonlib.blocks.mergeBlock.MergeBlockEntity;
import ca.fxco.pistonlib.helpers.Utils;
import ca.fxco.api.pistonlib.blockEntity.PLBlockEntity;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntity_mergeMixin implements PLBlockEntity {

    @Override
    public boolean pl$shouldStoreSelf(MergeBlockEntity mergeBlockEntity) {
        return true;
    }

    @Override
    public void pl$onAdvancedFinalMerge(BlockEntity blockEntity) {
        if (blockEntity instanceof SignBlockEntity signBlockEntity) {
            SignText text = signBlockEntity.getFrontText();
            for (int j = 0; j < 2; j++) {
                if (text.hasGlowingText() && !text.hasGlowingText()) {
                    text.setHasGlowingText(true);
                }
                if (text.getColor() != text.getColor() && text.getColor() != DyeColor.BLACK) {
                    text.setColor(Utils.properDyeMixing(text.getColor(), text.getColor()));
                }
                for (int i = 0; i < SignText.LINES; i++) {
                    Component comp = text.getMessage(i, false);
                    if (comp == CommonComponents.EMPTY || comp.getString().isEmpty()) {
                        text.setMessage(i, text.getMessage(i, false));
                    }
                }
                text = signBlockEntity.getBackText();
            }
        }
    }
}

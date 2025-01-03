package ca.fxco.pistonlib.mixin;

import ca.fxco.api.pistonlib.block.state.PLBlockStateBase;
import ca.fxco.api.pistonlib.pistonLogic.PistonMoveBehavior;
import ca.fxco.pistonlib.PistonLibConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public class BlockStateBase_coreMixin implements PLBlockStateBase {

    @Unique
    private PistonMoveBehavior pl$pistonMoveBehaviorOverride = PistonMoveBehavior.DEFAULT;

    @Shadow
    private Block getBlock() { return null; }

    @Shadow
    private BlockState asState() { return null; }

    @Override
    public Block pl$getBlock() {
        return this.getBlock();
    }

    @Override
    public BlockState pl$asState() {
        return this.asState();
    }

    @Override
    public void pl$setPistonMoveBehaviorOverride(PistonMoveBehavior override) {
        this.pl$pistonMoveBehaviorOverride = override;
    }

    @Override
    public PistonMoveBehavior pl$getPistonMoveBehaviorOverride() {
        return this.pl$pistonMoveBehaviorOverride;
    }

    //
    // Override Behavior
    //

    @Inject(
            method = "getPistonPushReaction",
            at = @At("HEAD"),
            cancellable = true
    )
    private void pl$overridePushReaction(CallbackInfoReturnable<PushReaction> cir) {
        if (PistonLibConfig.behaviorOverrideApi) {
            if (pl$pistonMoveBehaviorOverride.isPresent()) {
                cir.setReturnValue(pl$pistonMoveBehaviorOverride.getPushReaction());
            }
        }
    }
}

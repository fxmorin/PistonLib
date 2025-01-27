package ca.fxco.pistonlib.mixin.behavior;

import ca.fxco.pistonlib.PistonLibConfig;
import ca.fxco.pistonlib.helpers.PistonLibBehaviorManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlock_behaviorMixin {

    @WrapOperation(
            method = "isPushable",
            slice = @Slice(
                    from = @At(
                            value = "RETURN",
                            ordinal = 1
                    ),
                    to = @At(
                            value = "RETURN",
                            ordinal = 2
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;" +
                            "is(Lnet/minecraft/world/level/block/Block;)Z"
            )
    )
    private static boolean pl$overrideObsidianPushReaction(BlockState instance, Block block,
                                                           Operation<Boolean> original) {
        // Several blocks are made immovable with explicit checks. To override the
        // push reaction of these blocks we make these checks fail.
        if (original.call(instance, block)) {
            if (PistonLibConfig.behaviorOverrideApi) {
                return !PistonLibBehaviorManager.getOverride(instance).isPresent();
            }
            return true;
        }
        return false;
    }

    @WrapOperation(
            method = "isPushable",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;" +
                            "getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"
            )
    )
    private static float pl$overrideBedrockPushReaction(BlockState instance, BlockGetter level,
                                                        BlockPos pos, Operation<Float> original) {
        // Several blocks are made immovable due to having "negative mining speed".
        // To override the push reaction of these blocks we return a non-negative
        // mining speed instead.
        float destroySpeed = original.call(instance, level, pos);

        if (PistonLibConfig.behaviorOverrideApi && destroySpeed == -1.0F &&
                PistonLibBehaviorManager.getOverride(instance).isPresent()) {
            return 0.0F;
        }

        return destroySpeed;
    }

    @WrapOperation(
            method = "isPushable",
            slice = @Slice(
                    from = @At(
                            value = "RETURN",
                            ordinal = 4
                    ),
                    to = @At(
                            value = "RETURN",
                            ordinal = 5
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;" +
                            "is(Lnet/minecraft/world/level/block/Block;)Z"
            )
    )
    private static boolean pl$overridePistonPushReaction(BlockState instance, Block block, Operation<Boolean> original,
                                                         BlockState state, Level level, BlockPos pos,
                                                         Direction moveDir, boolean allowDestroy,
                                                         Direction pistonFacing) {
        return original.call(state, block) &&
                (!PistonLibConfig.behaviorOverrideApi || !PistonLibBehaviorManager.getOverride(state).isPresent());
    }
}

package ca.fxco.pistonlib.mixin.movingPiston;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import ca.fxco.pistonlib.base.ModTags;

import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(TerrainParticle.class)
public class TerrainParticle_movingPistonMixin {

    @Redirect(
        method = "createTerrainParticle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private static boolean allMovingPistons(BlockState state, Block block) {
        return state.is(ModTags.MOVING_PISTONS);
    }
}

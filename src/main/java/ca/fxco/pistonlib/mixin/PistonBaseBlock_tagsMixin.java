package ca.fxco.pistonlib.mixin;

import ca.fxco.api.pistonlib.block.PLPistonController;
import ca.fxco.api.pistonlib.pistonLogic.controller.PistonController;
import ca.fxco.pistonlib.base.ModPistonFamilies;
import ca.fxco.pistonlib.base.ModTags;
import ca.fxco.pistonlib.pistonLogic.controller.VanillaPistonController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/*
 * In this mixin we basically change all the !state.is(PISTON) and regular piston to instead check against the
 * PISTONS TagKey, so that all pistons work the same with each other (Including vanilla)
 *
 * We also replace any instances of PistonStructureResolver with the custom ConfigurablePistonStructureResolver.
 * This ensures even vanilla pistons will respect custom movability and sticky behavior.
 */
@Mixin(PistonBaseBlock.class)
public class PistonBaseBlock_tagsMixin implements PLPistonController {

    @Unique
    private static final PistonController VANILLA_CONTROLLER_DEFAULT = pl$createVanillaController(PistonType.DEFAULT);
    @Unique
    private static final PistonController VANILLA_CONTROLLER_STICKY  = pl$createVanillaController(PistonType.STICKY);

    @Shadow
    @Final
    private boolean isSticky;

    @Redirect(
            method = "checkIfExtend(Lnet/minecraft/world/level/Level;" +
                    "Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/world/level/block/piston/PistonStructureResolver"
            )
    )
    private PistonStructureResolver pl$customStructureResolver1(Level level, BlockPos pos,
                                                                Direction facing, boolean extend) {
        return pl$newStructureResolver(level, pos, facing, extend);
    }

    @Redirect(
        method = "triggerEvent(Lnet/minecraft/world/level/block/state/BlockState;" +
                 "Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;II)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;isPushable(" +
                     "Lnet/minecraft/world/level/block/state/BlockState;" +
                     "Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;" +
                     "Lnet/minecraft/core/Direction;ZLnet/minecraft/core/Direction;)Z"
        )
    )
    private boolean pl$modifyIsMovable(BlockState state, Level level, BlockPos pos,
                                       Direction moveDir, boolean allowDestroy, Direction pistonFacing) {
        return pl$getPistonController().canMoveBlock(state, level, pos, moveDir, allowDestroy, pistonFacing);
    }

    @Redirect(
        method = "triggerEvent(Lnet/minecraft/world/level/block/state/BlockState;" +
                 "Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;II)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z",
            ordinal = 1
        )
    )
    private boolean pl$allPistons(BlockState state, Block block) {
        return state.is(ModTags.PISTONS);
    }

    @Redirect(
        method = "triggerEvent(Lnet/minecraft/world/level/block/state/BlockState;" +
                 "Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;II)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z",
            ordinal = 2
        )
    )
    private boolean pl$skipIsPistonCheck(BlockState state, Block block) {
        return false;
    }

    @Redirect(
        method = "moveBlocks(Lnet/minecraft/world/level/Level;" +
                "Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Z",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/level/block/piston/PistonStructureResolver"
        )
    )
    private PistonStructureResolver pl$customStructureResolver2(Level level, BlockPos pos,
                                                                Direction facing, boolean extend) {
        return pl$newStructureResolver(level, pos, facing, extend);
    }

    @Override
    public PistonController pl$getPistonController() {
        return this.isSticky ? VANILLA_CONTROLLER_STICKY : VANILLA_CONTROLLER_DEFAULT;
    }

    @Unique
    private PistonStructureResolver pl$newStructureResolver(Level level, BlockPos pos,
                                                            Direction facing, boolean extend) {
        // the basic pistons should act exactly as vanilla pistons anyway
        return pl$getPistonController().newStructureResolver(level, pos, facing, extend ? 0 : 1, extend);
    }

    @Unique
    private static PistonController pl$createVanillaController(final PistonType type) {
        return new VanillaPistonController(ModPistonFamilies.VANILLA, type);
    }
}

package ca.fxco.pistonlib.pistonLogic.families;

import ca.fxco.api.pistonlib.pistonLogic.families.PistonBehavior;
import ca.fxco.api.pistonlib.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.base.ModPistonFamilies;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;

import lombok.RequiredArgsConstructor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

@RequiredArgsConstructor
public class ModPistonFamily extends PistonFamily {

    public boolean hasCustomTextures() {
        return true; // Handled in DataGenPistonFamily
    }

    public static ModPistonFamily of(PistonBehavior behavior) {
        return PistonLib.DATAGEN_ACTIVE ? new DataGenPistonFamily(behavior, true) : new ModPistonFamily(behavior);
    }

    public static ModPistonFamily of(PistonBehavior behavior, boolean hasCustomTextures) {
        return PistonLib.DATAGEN_ACTIVE ? new DataGenPistonFamily(behavior, hasCustomTextures) : new ModPistonFamily(behavior);
    }
}

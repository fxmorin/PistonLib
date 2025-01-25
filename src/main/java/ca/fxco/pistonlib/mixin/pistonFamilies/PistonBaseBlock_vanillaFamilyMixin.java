package ca.fxco.pistonlib.mixin.pistonFamilies;

import org.spongepowered.asm.mixin.Mixin;

import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamilyMember;
import ca.fxco.pistonlib.base.ModPistonFamilies;

import net.minecraft.world.level.block.piston.PistonBaseBlock;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlock_vanillaFamilyMixin implements PistonFamilyMember {

    @Override
    public PistonFamily getFamily() {
        return ModPistonFamilies.VANILLA;
    }

    @Override
    public void setFamily(PistonFamily family) {
    }
}

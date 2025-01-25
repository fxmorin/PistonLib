package ca.fxco.pistonlib.pistonLogic.families;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonBehavior;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;

public class ModPistonFamily extends PistonFamily {

    public ModPistonFamily(PistonBehavior behavior) {
        super(behavior);
    }

    @Override
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

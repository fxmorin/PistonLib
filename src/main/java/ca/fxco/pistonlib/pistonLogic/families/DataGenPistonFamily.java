package ca.fxco.pistonlib.pistonLogic.families;

import ca.fxco.api.pistonlib.pistonLogic.families.PistonBehavior;

public class DataGenPistonFamily extends ModPistonFamily {

    private final boolean customTextures;

    public DataGenPistonFamily(PistonBehavior behavior, boolean hasCustomTextures) {
        super(behavior);

        this.customTextures = hasCustomTextures;
    }

    @Override
    public boolean hasCustomTextures() {
        return customTextures;
    }
}

package ca.fxco.pistonlib.mixin.behavior;

import ca.fxco.api.pistonlib.block.BlockMoveBehavior;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MovingPistonBlock.class)
public abstract class MovingPistonBlock_behaviorMixin implements BlockMoveBehavior {

    @Override
    public boolean pl$canOverridePistonMoveBehavior() {
        return false;
    }
}

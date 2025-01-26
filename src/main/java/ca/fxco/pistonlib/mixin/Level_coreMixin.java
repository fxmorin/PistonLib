package ca.fxco.pistonlib.mixin;

import ca.fxco.pistonlib.api.level.PLLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public class Level_coreMixin implements PLLevel {
}

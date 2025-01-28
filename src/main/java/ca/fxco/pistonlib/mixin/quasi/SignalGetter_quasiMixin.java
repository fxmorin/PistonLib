package ca.fxco.pistonlib.mixin.quasi;

import ca.fxco.pistonlib.api.level.PLLevel;
import net.minecraft.world.level.SignalGetter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignalGetter.class)
public interface SignalGetter_quasiMixin extends PLLevel { }

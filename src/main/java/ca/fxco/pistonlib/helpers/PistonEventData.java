package ca.fxco.pistonlib.helpers;

import ca.fxco.pistonlib.api.block.PLPistonController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;

public record PistonEventData<P extends Block & PLPistonController>(P pistonBlock, BlockPos pos,
                                                                    Direction dir, boolean extend) {
}

package ca.fxco.pistonlib.api.pistonLogic.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;

// TODO-API: JavaDoc
public interface StructureResolver {

    @FunctionalInterface
    interface Factory<T extends PistonStructureResolver & StructureResolver> {

        T create(Level level, BlockPos pos, Direction facing, int length, boolean extend);

    }
}

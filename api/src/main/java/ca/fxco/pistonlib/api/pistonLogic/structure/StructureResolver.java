package ca.fxco.pistonlib.api.pistonLogic.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;

/**
 * The Structure resolver.
 * This exists to reduce the amount of breaking changes in the API in the future.
 * PistonLib currently uses minecraft's {@link PistonStructureResolver} as its base.
 *
 * @author FX
 * @since 1.2.0
 */
public interface StructureResolver {

    /**
     * Factory used to create Structure resolvers
     *
     * @param <T> The type of structure resolver we want to create
     * @since 1.2.0
     */
    @FunctionalInterface
    interface Factory<T extends PistonStructureResolver & StructureResolver> {

        /**
         * Creates a new structure resolver
         *
         * @param level   The level we're creating the structure resolver in
         * @param pos     The position to start the resolver at
         * @param facing  The direction to start the resolver at
         * @param length  How long the piston is
         * @param extend  If the resolver should be extending or retracting
         * @return The new structure resolver
         * @since 1.2.0
         */
        T create(Level level, BlockPos pos, Direction facing, int length, boolean extend);

    }
}

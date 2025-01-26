package ca.fxco.pistonlib.api.pistonLogic.structure;

import ca.fxco.pistonlib.api.pistonLogic.base.PLMovingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Consumer;

/**
 * An instance of the entire piston structure as a group.
 * Used for multiple optimizations, such as culling blocks being pushed at the same time.
 * Holds block entities of the moving structure.
 *
 * @author FX
 * @since 1.0.4
 */
public interface StructureGroup {

    /**
     * Check if this group has been initialized.
     *
     * @return {@code true} if it had initialized, otherwise {@code false}
     * @since 1.0.4
     */
    default boolean hasInitialized() {
        return false;
    }

    /**
     * Adds a block entity to the structure group.
     *
     * @param blockEntity block entity to add to structure group
     * @param <P> The PistonMovingBlockEntity type which implements PLMovingBlockEntity
     * @since 1.2.0
     */
    <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void add(P blockEntity);

    /**
     * Adds a block entity to the structure group by index.
     *
     * @param index       index of block entity
     * @param blockEntity block entity to add to structure group
     * @param <P> The PistonMovingBlockEntity type which implements PLMovingBlockEntity
     * @since 1.2.0
     */
    <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void add(int index, P blockEntity);

    /**
     * Removes a block entity from structure group.
     *
     * @param blockEntity block entity to remove
     * @param <P> The PistonMovingBlockEntity type which implements PLMovingBlockEntity
     * @since 1.2.0
     */
    <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void remove(P blockEntity);

    /**
     * Removes a block entity from structure group by index.
     *
     * @param index index of block entity to remove
     * @since 1.0.4
     */
    void remove(int index);

    /**
     * Get a block entity from the structure group by index.
     *
     * @param index index of block entity
     * @param <P> The PistonMovingBlockEntity type which implements PLMovingBlockEntity
     * @return block entity
     * @since 1.2.0
     */
    <P extends PistonMovingBlockEntity & PLMovingBlockEntity> P get(int index);

    /**
     * Get the size of the structure group.
     *
     * @return amount of block entities this structure has
     * @since 1.0.4
     */
    int size();

    /**
     * Get a state within the structure group.
     *
     * @param blockPos block position to get block state at
     * @return block state at the block position
     * @since 1.0.4
     */
    BlockState getState(BlockPos blockPos);

    //
    // Grouped methods
    //

    /**
     * Calls a consumer against all group children including the controller
     *
     * @param action action to execute
     * @param <P> The PistonMovingBlockEntity type which implements PLMovingBlockEntity
     * @since 1.2.0
     */
    <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void forEach(Consumer<P> action);

    /**
     * Calls a consumer against all group children except the controller
     *
     * @param action action to execute
     * @param <P> The PistonMovingBlockEntity type which implements PLMovingBlockEntity
     * @since 1.2.0
     */
    <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void forNonControllers(Consumer<P> action);

    //
    // Saving / Loading
    //

    /**
     * Load structure group nbt.
     *
     * @param level        the level used to load the structure group
     * @param blockPosList the list of block positions to populate
     * @since 1.2.0
     */
    void load(Level level, List<BlockPos> blockPosList);

    /**
     * Saves additional information about the structure group to nbt.
     *
     * @param nbt the tag to add additional info to
     * @since 1.0.4
     */
    void saveAdditional(CompoundTag nbt);

    /**
     * Creates a Structure Group based on the environment.
     *
     * @param level the level used to check the environment
     * @return If on the client, a {@link ClientStructureGroup}, otherwise a {@link ServerStructureGroup}
     * @since 1.2.0
     */
    static StructureGroup create(Level level) { // TODO-API: Move out of here...
        /*if (level.isClientSide) {
            return new ClientStructureGroup(); // Holds rendering cache
        }
        return new ServerStructureGroup();*/
        return null;
    }
}

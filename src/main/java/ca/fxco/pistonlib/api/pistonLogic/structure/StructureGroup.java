package ca.fxco.pistonlib.api.pistonLogic.structure;

import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.pistonLogic.structureGroups.ClientStructureGroup;
import ca.fxco.pistonlib.pistonLogic.structureGroups.ServerStructureGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

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
     */
    default boolean hasInitialized() {
        return false;
    }

    /**
     * Adds a block entity to the structure group.
     *
     * @param blockEntity block entity to add to structure group
     */
    void add(BasicMovingBlockEntity blockEntity);

    /**
     * Adds a block entity to the structure group by index.
     *
     * @param index       index of block entity
     * @param blockEntity block entity to add to structure group
     */
    void add(int index, BasicMovingBlockEntity blockEntity);

    /**
     * Removes a block entity from structure group.
     *
     * @param blockEntity block entity to remove
     */
    void remove(BasicMovingBlockEntity blockEntity);

    /**
     * Removes a block entity from structure group by index.
     *
     * @param index index of block entity to remove
     */
    void remove(int index);

    /**
     * Get a block entity from the structure group by index.
     *
     * @param index index of block entity
     * @return block entity
     */
    BasicMovingBlockEntity get(int index);

    /**
     * Get the size of the structure group.
     *
     * @return amount of block entities this structure has
     */
    int size();

    /**
     * Get a state within the structure group.
     *
     * @param blockPos block position to get block state at
     * @return block state at the block position
     */
    BlockState getState(BlockPos blockPos);

    //
    // Grouped methods
    //

    /**
     * Calls a consumer against all group children including the controller
     *
     * @param action action to execute
     * @since 1.0.4
     */
    void forEach(Consumer<BasicMovingBlockEntity> action);

    /**
     * Calls a consumer against all group children except the controller
     *
     * @param action action to execute
     * @since 1.0.4
     */
    void forNonControllers(Consumer<BasicMovingBlockEntity> action);

    /**
     * Saves additional information about the structure group to nbt.
     *
     * @param nbt the tag to add additional info to
     */
    void saveAdditional(CompoundTag nbt);

    /**
     * Creates a Structure Group based on the environment.
     *
     * @param level the level used to check the environment
     * @return If on the client, a {@link ClientStructureGroup}, otherwise a {@link ServerStructureGroup}
     */
    static ServerStructureGroup create(Level level) {
        if (level.isClientSide) {
            return new ClientStructureGroup(); // Holds rendering cache
        }
        return new ServerStructureGroup();
    }
}

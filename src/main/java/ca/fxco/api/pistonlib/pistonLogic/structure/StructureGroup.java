package ca.fxco.api.pistonlib.pistonLogic.structure;

import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.pistonLogic.structureGroups.ClientStructureGroup;
import ca.fxco.pistonlib.pistonLogic.structureGroups.ServerStructureGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

/**
 * holds block entities of the moving structure
 *
 * @author FX
 * @since 1.0.4
 */
public interface StructureGroup {

    /**
     * @return true if it had initialized
     */
    default boolean hasInitialized() {
        return false;
    }

    /**
     * adds block entity to the structure group
     *
     * @param blockEntity block entity to add to structure group
     */
    void add(BasicMovingBlockEntity blockEntity);

    /**
     * adds block entity to the structure group by index
     *
     * @param index index of block entity
     * @param blockEntity block entity to add to structure group
     */
    void add(int index, BasicMovingBlockEntity blockEntity);

    /**
     * removes block entity from structure group
     *
     * @param blockEntity block entity to remove
     */
    void remove(BasicMovingBlockEntity blockEntity);

    /**
     * removes block entity from structure group by index
     *
     * @param index index of block entity to remove
     */
    void remove(int index);

    /**
     * @param index index of block entity
     * @return block entity
     */
    BasicMovingBlockEntity get(int index);

    /**
     * @return amount of block entities this structure has
     */
    int size();

    /**
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

    void saveAdditional(CompoundTag nbt);

    static ServerStructureGroup create(Level level) {
        if (level.isClientSide) {
            return new ClientStructureGroup(); // Holds rendering cache
        }
        return new ServerStructureGroup();
    }
}

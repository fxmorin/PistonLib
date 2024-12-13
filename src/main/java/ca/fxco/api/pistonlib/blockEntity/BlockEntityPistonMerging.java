package ca.fxco.api.pistonlib.blockEntity;

import ca.fxco.pistonlib.blocks.mergeBlock.MergeBlockEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * This is for handling merging the block entities.
 * @author FX
 * @since 1.0.4
 */
public interface BlockEntityPistonMerging {

    //
    // These methods are to control if merging should happen
    //

    /**
     * @param state block state of the block
     * @param mergingIntoState block state to merge into
     * @param dir direction it's being pushed
     * @return true if it will be able to merge both states together
     * @since 1.0.4
     */
    boolean pl$canMerge(BlockState state, BlockState mergingIntoState, Direction dir);

    /**
     * While merging with a block, is this block able to merge with other blocks from other directions?
     * @param state block state of the block
     * @param mergingIntoState block state to merge into
     * @param dir direction it being pushed
     * @param currentlyMerging is currently mering
     * @return true if it can multi merge with blocks from other directions
     * @since 1.0.4
     */
    boolean pl$canMultiMerge(BlockState state, BlockState mergingIntoState, Direction dir, Map<Direction, MergeBlockEntity.MergeData> currentlyMerging);

    /**
     *
     * @param state block state of the block
     * @param neighborState neighbor state of the block
     * @param dir direction its being pulled
     * @return true if it will be able to unmerge into two different states
     * @since 1.0.4
     */
    boolean pl$canUnMerge(BlockState state, BlockState neighborState, Direction dir);


    //
    // These methods are called to determine what to do with the block entity when merging
    //

    /**
     * Used for Advanced Final Merging. To use `onAdvancedFinalMerge` this method needs to return true.
     * This defines if this block entity should be saved in the merge block.
     * If it's not saved, the `onAdvancedFinalMerge` will not run for this block entity and the data from this
     * block entity will not be stored.
     * @param mergeBlockEntity block entity to store
     * @return true if this block entity should be saved in the merge block
     * @since 1.0.4
     */
    boolean pl$shouldStoreSelf(MergeBlockEntity mergeBlockEntity);


    //
    // These methods are called when merging start (onMerge) and merging end (onAdvancedFinalMerge)
    // They are only called for blocks merging into another block. Not initial blocks
    //

    /**
     * @param mergeBlockEntity merged block entity
     * @param dir direction its moving
     * @since 1.0.4
     */
    void pl$onMerge(MergeBlockEntity mergeBlockEntity, Direction dir);

    /**
     * When the merge is done, this method will be called for all saved block entities.
     * This is used to modify the block entity. If no block entity is present to modify, this method is not called!
     * @param blockEntity block entity it's called for
     * @since 1.0.4
     */
    void pl$onAdvancedFinalMerge(BlockEntity blockEntity);


    //
    // These methods are called when unmerging
    //

    /**
     * Determines what blocks the block entity should unmerge into.
     * Return null to call the blockstate `doUnMerge` method
     * The first block in the pair is the block that will be pulled out
     * @param state block state of the block
     * @param dir direction its moving
     * @return pair of block states after unmerge
     * @since 1.0.4
     */
    @Nullable Pair<BlockState, BlockState> pl$doUnMerge(BlockState state, Direction dir);

    /**
     * If the block entity should unmerge also or if it should stay where it is.
     * If it stays it won't replace the block entity
     * @param state block state of the block
     * @param dir direction its moving
     * @return true if it should unmerge
     * @since 1.0.4
     */
    boolean pl$shouldUnMergeBlockEntity(BlockState state, Direction dir);


    //
    // These methods are only for initial block entities. For blocks that got other blocks merged into them
    //

    /**
     * This method only gets called if this is the block entity of the block getting converted to a merge block
     * If false, the block entity is removed and the merge will finish like normally.
     * @return true if block entity should do initial merging
     * @since 1.0.4
     */
    boolean pl$doInitialMerging();

    /**
     * When the merge is done, before all the other `onAdvancedFinalMerge` methods
     * @param finalState final merged block state
     * @param mergedData merged data about block entity
     * @since 1.0.4
     */
    void pl$beforeInitialFinalMerge(BlockState finalState, Map<Direction, MergeBlockEntity.MergeData> mergedData);

    /**
     * When the merge is done, after all the other `onAdvancedFinalMerge` methods
     * @param finalState final merged block state
     * @param mergedData merged data about block entity
     * @since 1.0.4
     */
    void pl$afterInitialFinalMerge(BlockState finalState, Map<Direction, MergeBlockEntity.MergeData> mergedData);

}

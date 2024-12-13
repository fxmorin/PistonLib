package ca.fxco.api.pistonlib.block.state;

import ca.fxco.pistonlib.blocks.mergeBlock.MergeBlockEntity;
import ca.fxco.api.pistonlib.block.BlockPistonMerging;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface BlockStatePistonMerging {

    /**
     * These methods are only used if `usesConfigurablePistonMerging` return true
     * This allows for configurable and conditional mering/compression block logic
     * @return true if it should use configurable piston merging
     */
    boolean pl$usesConfigurablePistonMerging();


    /**
     * @param level of the block state
     * @param pos block pos of the block state
     * @param mergingIntoState block state to merge into
     * @param dir direction  being pushed
     * @return true if it will be able to merge both states together
     * @since 1.0.4
     */
    boolean pl$canMerge(BlockGetter level, BlockPos pos, BlockState mergingIntoState, Direction dir);

    /**
     * @param level of the block state
     * @param pos block pos of the block state
     * @param pushDir direction it being pushed
     * @return true if the block can be merged from a given side or can be merged from that side.
     * Usually opposite of pushDirection
     * @since 1.0.4
     */
    boolean pl$canMergeFromSide(BlockGetter level, BlockPos pos, Direction pushDir);

    /**
     * @param level of the block state
     * @param pos block pos of the block state
     * @param mergingIntoState block state to merge into
     * @param dir direction it being pushed
     * @return the merged state
     * @since 1.0.4
     */
    BlockState pl$doMerge(BlockGetter level, BlockPos pos, BlockState mergingIntoState, Direction dir);

    /**
     * This must return true if you want to be able to merge more than one
     * block at a time using `canMultiMerge` and `doMultiMerge`
     * @return true if it can multi merge
     * @since 1.0.4
     */
    boolean pl$canMultiMerge();

    /**
     * While merging with a block, is this block state able to merge with other block states from other directions?
     * @param level of the block state
     * @param pos block pos of the block
     * @param mergingIntoState block state to merge into
     * @param dir direction it being pushed
     * @param currentlyMerging is currently mering
     * @return true if it can multi merge with blocks from other directions
     * @since 1.0.4
     */
    boolean pl$canMultiMerge(BlockGetter level, BlockPos pos, BlockState mergingIntoState, Direction dir, Map<Direction, MergeBlockEntity.MergeData> currentlyMerging);

    /**
     * merge states into one
     * @param level of the block
     * @param pos block pos of the block
     * @param states states around the block
     * @param mergingIntoState block state to merge into
     * @return the merged state
     * @since 1.0.4
     */
    BlockState pl$doMultiMerge(BlockGetter level, BlockPos pos, Map<Direction,BlockState> states, BlockState mergingIntoState);


    /**
     * @param level of the block state
     * @param pos block pos of the block state
     * @param neighborState block state of block state's neighbor
     * @param dir direction it being pulled
     * @return true if it will be able to unmerge into two different states
     * @since 1.0.4
     */
    boolean pl$canUnMerge(BlockGetter level, BlockPos pos, BlockState neighborState, Direction dir);

    /**
     * @param level of the block state
     * @param pos block pos of the block state
     * @param dir direction it being pulled
     * @return the block states that it should unmerge into.
     * The first block state in the pair is the block state that will be pulled out
     * @since 1.0.4
     */
    @Nullable Pair<BlockState, BlockState> pl$doUnMerge(BlockGetter level, BlockPos pos, Direction dir);

    /**
     * This method determines when the block entity should be used:
     * -     NEVER = Block entity will be skipped completely
     * -   MERGING = Block entity will be used to check merging conditions
     * - UNMERGING = Block entity will be used to check unmerging conditions
     * -    ALWAYS = Block entity will always be checked
     * State checks will always happen before block entity checks
     * Skipping won't get the block entity at all, this is done for performance reasons.
     * It allows us to quickly know if the block entity should be loaded and checked against
     * @see BlockPistonMerging.MergeRule
     * @return merge rule this block entity uses
     * @since 1.0.4
     */
    BlockPistonMerging.MergeRule pl$getBlockEntityMergeRules();
}

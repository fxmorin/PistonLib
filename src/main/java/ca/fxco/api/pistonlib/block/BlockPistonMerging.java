package ca.fxco.api.pistonlib.block;

import ca.fxco.pistonlib.blocks.mergeBlock.MergeBlockEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Both blocks that are attempting to merge should have the same checks!
 * @author FX
 * @since 1.0.4
 */
public interface BlockPistonMerging {

    /**
     * @return must return true in order for the configurable piston merging to be used!
     * @since 1.0.4
     */
    boolean pl$usesConfigurablePistonMerging();


    /**
     * @param state block state of the block
     * @param level of the block
     * @param pos block pos of the block
     * @param mergingIntoState block state to merge into
     * @param dir direction  being pushed
     * @return true if it will be able to merge both states together
     * @since 1.0.4
     */
    boolean pl$canMerge(BlockState state, BlockGetter level, BlockPos pos, BlockState mergingIntoState, Direction dir);

    //

    /**
     * @param state block state of the block
     * @param level of the block
     * @param pos block pos of the block
     * @param pushDir direction it being pushed
     * @return true if the block can be merged from a given side or can be merged from that side.
     * Usually opposite of pushDirection
     * @since 1.0.4
     */
    boolean pl$canMergeFromSide(BlockState state, BlockGetter level, BlockPos pos, Direction pushDir);

    /**
     * @param state block state of the block
     * @param level of the block
     * @param pos block pos of the block
     * @param mergingIntoState block state to merge into
     * @param dir direction it being pushed
     * @return the merged state
     * @since 1.0.4
     */
    BlockState pl$doMerge(BlockState state, BlockGetter level, BlockPos pos, BlockState mergingIntoState, Direction dir);


    /**
     * This must return true if you want to be able to merge more than one
     * block at a time using `canMultiMerge` and `doMultiMerge`
     * @return true if it can multi merge
     * @since 1.0.4
     */
    boolean pl$canMultiMerge();

    /**
     * While merging with a block, is this block able to merge with other blocks from other directions?
     * @param state block state of the block
     * @param level of the block
     * @param pos block pos of the block
     * @param mergingIntoState block state to merge into
     * @param dir direction it being pushed
     * @param currentlyMerging is currently mering
     * @return true if it can multi merge with blocks from other directions
     * @since 1.0.4
     */
    boolean pl$canMultiMerge(BlockState state, BlockGetter level, BlockPos pos, BlockState mergingIntoState, Direction dir, Map<Direction, MergeBlockEntity.MergeData> currentlyMerging);

    /**
     * merge states into one
     * @param level of the block
     * @param pos block pos of the block
     * @param states states around the block
     * @param mergingIntoState block state to merge into
     * @return the merged state
     * @since 1.0.4
     */
    BlockState pl$doMultiMerge(BlockGetter level, BlockPos pos, Map<Direction, BlockState> states, BlockState mergingIntoState);

    /**
     * @param state block state of the block
     * @param level of the block
     * @param pos block pos of the block
     * @param neighborState block state of block's neighbor
     * @param dir direction it being pulled
     * @return true if it will be able to unmerge into two different states
     * @since 1.0.4
     */
    boolean pl$canUnMerge(BlockState state, BlockGetter level, BlockPos pos, BlockState neighborState, Direction dir);

    //

    /**
     * @param state block state of the block
     * @param level of the block
     * @param pos block pos of the block
     * @param dir direction it being pulled
     * @return the block states that it should unmerge into.
     * The first block in the pair is the block that will be pulled out
     * @since 1.0.4
     */
    @Nullable Pair<BlockState, BlockState> pl$doUnMerge(BlockState state, BlockGetter level, BlockPos pos, Direction dir);

    /**
     * This method determines when the block entity should be used:
     * -     NEVER = Block entity will be skipped completely
     * -   MERGING = Block entity will be used to check merging conditions
     * - UNMERGING = Block entity will be used to check unmerging conditions
     * -    ALWAYS = Block entity will always be checked
     * State checks will always happen before block entity checks
     * Skipping won't get the block entity at all, this is done for performance reasons.
     * It allows us to quickly know if the block entity should be loaded and checked against
     * @return merge rule this block entity uses
     * @since 1.0.4
     */
    MergeRule pl$getBlockEntityMergeRules();

    enum MergeRule {
        NEVER(false, false),
        MERGING(true, false),
        UNMERGING(false, true),
        ALWAYS(true, true);

        private final boolean merging;
        private final boolean unmerging;

        MergeRule(boolean merging, boolean unmerging) {
            this.merging = merging;
            this.unmerging = unmerging;
        }

        public boolean checkMerge() {
            return this.merging;
        }

        public boolean checkUnMerge() {
            return this.unmerging;
        }
    }
}

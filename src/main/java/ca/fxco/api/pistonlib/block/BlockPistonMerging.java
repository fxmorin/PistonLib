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
 * These methods are only used if {@link #pl$usesConfigurablePistonMerging}` returns {@code true}
 *
 * @author FX
 * @since 1.0.4
 */
public interface BlockPistonMerging {

    /**
     * This must return true in order for the configurable and
     * conditional merging/compression logic to be used!
     *
     * @return {@code true} if it should use configurable piston merging, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$usesConfigurablePistonMerging();

    /**
     * Checks if this block can be merged.
     *
     * @param state            block state of the block
     * @param level            of the block state
     * @param pos              block position of the block state
     * @param mergingIntoState block state to merge into
     * @param dir              direction  being pushed
     * @return {@code true} if it will be able to merge both states together, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canMerge(BlockState state, BlockGetter level, BlockPos pos, BlockState mergingIntoState, Direction dir);

    /**
     * Checks if this block can be merged from a specific side.
     * Usually opposite of pushDirection.
     *
     * @param state   block state of the block
     * @param level   of the block state
     * @param pos     block position of the block state
     * @param pushDir direction it being pushed
     * @return {@code true} if the block can be merged from a given side or can be merged from that side,
     *  otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canMergeFromSide(BlockState state, BlockGetter level, BlockPos pos, Direction pushDir);

    /**
     * Merges two states together.
     *
     * @param state            block state of the block
     * @param level            of the block state
     * @param pos              block position of the block state
     * @param mergingIntoState block state to merge into
     * @param dir              direction it being pushed
     * @return the merged state
     * @since 1.0.4
     */
    BlockState pl$doMerge(BlockState state, BlockGetter level, BlockPos pos,
                          BlockState mergingIntoState, Direction dir);


    /**
     * This must return true if you want to be able to merge more than one
     * block at a time using {@link #pl$canMultiMerge} and {@link #pl$doMultiMerge}
     *
     * @return {@code true} if it can multi merge, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canMultiMerge();

    /**
     * While merging with a block, is this block state able to merge with other block states from other directions?
     *
     * @param state            block state of the block
     * @param level            of the block state
     * @param pos              block position of the block
     * @param mergingIntoState block state to merge into
     * @param dir              direction it being pushed
     * @param currentlyMerging is currently mering
     * @return {@code true} if it can multi merge with blocks from other directions, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canMultiMerge(BlockState state, BlockGetter level, BlockPos pos, BlockState mergingIntoState,
                             Direction dir, Map<Direction, MergeBlockEntity.MergeData> currentlyMerging);

    /**
     * Merge multiple states into one
     *
     * @param level            of the block
     * @param pos              block position of the block
     * @param states           states around the block
     * @param mergingIntoState block state to merge into
     * @return the merged state
     * @since 1.0.4
     */
    BlockState pl$doMultiMerge(BlockGetter level, BlockPos pos, Map<Direction, BlockState> states,
                               BlockState mergingIntoState);

    /**
     * Checks if this block can un-merge.
     *
     * @param state         block state of the block
     * @param level         of the block state
     * @param pos           block position of the block state
     * @param neighborState block state of block state's neighbor
     * @param dir           direction it being pulled
     * @return {@code true} if it will be able to unmerge into two different states, otherwise {@code false}
     * @since 1.0.4
     */
    boolean pl$canUnMerge(BlockState state, BlockGetter level, BlockPos pos, BlockState neighborState, Direction dir);

    /**
     * Un-merge this block into two different states.
     * The first block state in the pair is the block state that will be pulled out.
     *
     * @param state block state of the block
     * @param level of the block state
     * @param pos   block position of the block state
     * @param dir   direction it being pulled
     * @return the block states that it should unmerge into.
     * @since 1.0.4
     */
    @Nullable Pair<BlockState, BlockState> pl$doUnMerge(BlockState state, BlockGetter level, BlockPos pos, Direction dir);

    /**
     * This method determines when the block entity should be used:
     * -     NEVER = Block entity will be skipped completely
     * -   MERGING = Block entity will be used to check merging conditions
     * - UNMERGING = Block entity will be used to check unmerging conditions
     * -    ALWAYS = Block entity will always be checked
     * </br>
     * State checks will always happen before block entity checks.
     * Skipping won't get the block entity at all, this is done for performance reasons.
     * It allows us to quickly know if the block entity should be loaded and checked against.
     *
     * @see BlockPistonMerging.MergeRule
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

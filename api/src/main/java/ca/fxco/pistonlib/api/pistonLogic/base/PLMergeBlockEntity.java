package ca.fxco.pistonlib.api.pistonLogic.base;

import ca.fxco.pistonlib.api.block.MovingTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * PistonLib's Merge Block Entity interface.
 *
 * @author FX
 * @since 1.2.0
 */
public interface PLMergeBlockEntity {

    /**
     * Gets the merging blocks.
     *
     * @return A map of the merging blocks.
     * @since 1.2.0
     */
    Map<Direction, MergeData> getMergingBlocks();

    /**
     * Checks if the merge block allows merging from a specific side.
     * Should always be called before calling {@link #canMerge}
     *
     * @param pushDirection The direction to check if you can merge into
     * @return {@code true} if you can merge from this side, otherwise {@code false}
     * @since 1.2.0
     */
    boolean canMergeFromSide(Direction pushDirection);

    /**
     * Checks if you can merge a blockstate into the merge block, from a specific side.
     * Make sure to call {@link #canMergeFromSide} before calling this method.
     *
     * @param state The block state that you want to merge into the merge block
     * @param dir   The direction to check if you can merge into
     * @return {@code true} if you can merge from this side, otherwise {@code false}
     * @since 1.2.0
     */
    boolean canMerge(BlockState state, Direction dir);

    /**
     * Do a merge for a specific block from a side.
     *
     * @param state The block state that you want to merge into the merge block
     * @param dir   The direction to merge the block from
     * @since 1.2.0
     */
    void doMerge(BlockState state, Direction dir);

    /**
     * Do a merge for a specific block from a side.
     *
     * @param state The block state that you want to merge into the merge block
     * @param dir   The direction to merge the block from
     * @param speed The speed to merge the block at
     * @since 1.2.0
     */
    void doMerge(BlockState state, Direction dir, float speed);

    /**
     * Do a merge for a specific block from a side.
     *
     * @param state       The block state that you want to merge into the merge block
     * @param blockEntity The block entity to include in the merge, if it has one.
     * @param dir         The direction to merge the block from
     * @param speed       The speed to merge the block at
     * @since 1.2.0
     */
    void doMerge(BlockState state, @NotNull BlockEntity blockEntity, Direction dir, float speed);

    /**
     * The rendering offset that should be used on the X Axis.
     *
     * @param dir          The side that you're checking the offset for
     * @param partialTick  The rendering partial tick
     * @param progress     The current merge progress
     * @param lastProgress The last merge progress
     * @return The X offset for this merge
     * @since 1.2.0
     */
    float getXOff(Direction dir, float partialTick, float progress, float lastProgress);

    /**
     * The rendering offset that should be used on the Y Axis.
     *
     * @param dir          The side that you're checking the offset for
     * @param partialTick  The rendering partial tick
     * @param progress     The current merge progress
     * @param lastProgress The last merge progress
     * @return The Y offset for this merge
     * @since 1.2.0
     */
    float getYOff(Direction dir, float partialTick, float progress, float lastProgress);

    /**
     * The rendering offset that should be used on the Z Axis.
     *
     * @param dir          The side that you're checking the offset for
     * @param partialTick  The rendering partial tick
     * @param progress     The current merge progress
     * @param lastProgress The last merge progress
     * @return The Z offset for this merge
     * @since 1.2.0
     */
    float getZOff(Direction dir, float partialTick, float progress, float lastProgress);

    /**
     * MergeData is the data stored for an individual side.
     * It stores the merge state for the side.
     *
     * @author FX
     * @since 1.2.0
     */
    interface MergeData {

        /**
         * If this merge has a block entity
         *
         * @return {@code true} if this merge has a block entity, otherwise {@code false}
         * @since 1.2.0
         */
        boolean hasBlockEntity();

        /**
         * Get the block entity from this merge
         *
         * @return The block entity from this merge
         * @since 1.2.0
         */
        BlockEntity getBlockEntity();

        /**
         * Gets the block state of this merge
         *
         * @return The block state for this merge
         * @since 1.2.0
         */
        BlockState getState();

        /**
         * Gets the progress of this merge
         *
         * @return The progress of the merge, between 0-1
         * @since 1.2.0
         */
        float getProgress();

        /**
         * Gets the last progress of this merge
         *
         * @return The last progress of the merge, between 0-1
         * @since 1.2.0
         */
        float getLastProgress();

        /**
         * Gets the speed of this merge
         *
         * @return The speed of the merge
         * @since 1.2.0
         */
        float getSpeed();

        /**
         * Sets the progress of the merge
         *
         * @param progress The progress to use
         * @since 1.2.0
         */
        void setProgress(float progress);

        /**
         * Sets the last progress of the merge
         *
         * @param lastProgress The progress to use
         * @since 1.2.0
         */
        void setLastProgress(float lastProgress);

        /**
         * Sets both the current and last progress
         *
         * @param progress The progress to use
         * @since 1.2.0
         */
        void setAllProgress(float progress);

        /**
         * Sets the speed of the merge
         *
         * @param speed The speed to use
         * @since 1.2.0
         */
        void setSpeed(float speed);

        /**
         * Part of the moving tick API.
         * Allowing {@link MovingTickable} to work with the merging API.
         *
         * @param level The level the block is in
         * @param toPos The position that the block is moving to
         * @param dir   The direction that the block is moving in
         * @since 1.2.0
         */
        void onMovingTick(Level level, BlockPos toPos, Direction dir);
    }
}

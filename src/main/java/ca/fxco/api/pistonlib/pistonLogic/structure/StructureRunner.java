package ca.fxco.api.pistonlib.pistonLogic.structure;

/**
 * The entire piston move code as an abstracted instruction list.
 * Interface is used to move blocks with pistons (or without them)
 *
 * @author FX
 * @since 1.0.4
 */
public interface StructureRunner {

    /**
     * Removes the piston head, in order to be able to place the block its pulling in its place.
     *
     * @since 1.0.4
     */
    void taskRemovePistonHeadOnRetract();

    /**
     * Runs the structure resolver.
     * This sets the lists.
     *
     * @return {@code false} if it failed to resolve, otherwise {@code true}
     * @since 1.0.4
     */
    boolean taskRunStructureResolver();

    /**
     * Collect blocks to move, and remove block entities that are in the way of blocks being moved.
     *
     * @since 1.0.4
     */
    void taskSetPositionsToMove();

    /**
     * Merges blocks together
     *
     * @since 1.0.4
     */
    void taskMergeBlocks();

    /**
     * Destroys blocks
     *
     * @since 1.0.4
     */
    void taskDestroyBlocks();

    /**
     * Fix cached states being used and updates.
     * Handles fixing multiple vanilla dupe bugs.
     * This only runs if the config `pistonPushingCacheFix` is enabled.
     *
     * @since 1.0.4
     */
    void taskFixUpdatesAndStates();

    /**
     * Move blocks to their new positions.
     *
     * @since 1.0.4
     */
    void taskMoveBlocks();

    /**
     * If extending, this places the extending head in front of the piston.
     *
     * @since 1.0.4
     */
    void taskPlaceExtendingHead();

    /**
     * Remove left over blocks.
     *
     * @since 1.0.4
     */
    void taskRemoveLeftOverBlocks();

    /**
     * Do remove neighbor updates.
     * Sends updates from all the positions where blocks where removed.
     *
     * @since 1.0.4
     */
    void taskDoRemoveNeighborUpdates();

    /**
     * Do destroy neighbor updates.
     * Sends updates from all the positions where blocks where destroyed.
     * This is where the `pistonPushingCacheFix` option is implemented.
     *
     * @since 1.0.4
     */
    void taskDoDestroyNeighborUpdates();

    /**
     * Do move neighbor updates.
     * Sends updates from all the positions where blocks have been moved to.
     *
     * @since 1.0.4
     */
    void taskDoMoveNeighborUpdates();

    /**
     * Do un-merge logic.
     * Here we set all the un-merged blocks first.
     * Then we send the updates for all those changes.
     *
     * @since 1.0.4
     */
    void taskDoUnMergeUpdates();

    /**
     * Update neighbors at piston head on extending.
     *
     * @since 1.0.4
     */
    void taskDoPistonHeadExtendingUpdate();

    /**
     * Run the structure.
     *
     * @return {@code true} if it was able to move blocks, otherwise {@code false}
     */
    default boolean run() {
        taskRemovePistonHeadOnRetract();

        // Create the structure resolver lists
        if (!taskRunStructureResolver()) {
            return false;
        }

        // collect blocks to move
        taskSetPositionsToMove();

        taskMergeBlocks();

        // destroy blocks
        taskDestroyBlocks();

        // Fix cached states being used and updates
        taskFixUpdatesAndStates();

        // move blocks
        taskMoveBlocks();

        // place extending head
        taskPlaceExtendingHead();

        // remove left over blocks
        taskRemoveLeftOverBlocks();

        // do remove neighbor updates
        taskDoRemoveNeighborUpdates();

        // do destroy neighbor updates
        taskDoDestroyNeighborUpdates();

        // do move neighbor updates
        taskDoMoveNeighborUpdates();

        // do unmerge neighbor updates
        taskDoUnMergeUpdates();

        // update neighbors at piston head on extending
        taskDoPistonHeadExtendingUpdate();

        return true;
    }
}

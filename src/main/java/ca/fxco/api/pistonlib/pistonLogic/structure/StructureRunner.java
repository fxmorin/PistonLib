package ca.fxco.api.pistonlib.pistonLogic.structure;

/**
 * The entire piston move code as an abstracted instruction list.
 * interface is used to move blocks with pistons (or without them)
 *
 * @author FX
 * @since 1.0.4
 */
public interface StructureRunner {

    void taskRemovePistonHeadOnRetract();

    /**
     * Sets the lists
     *
     * @return false if it failed to resolve
     * @since 1.0.4
     */
    boolean taskRunStructureResolver();

    /**
     * collect blocks to move
     *
     * @since 1.0.4
     */
    void taskSetPositionsToMove();

    /**
     * merge blocks
     *
     * @since 1.0.4
     */
    void taskMergeBlocks();

    /**
     * destroy blocks
     *
     * @since 1.0.4
     */
    void taskDestroyBlocks();

    /**
     * Fix cached states being used and updates
     *
     * @since 1.0.4
     */
    void taskFixUpdatesAndStates();

    /**
     * move blocks
     *
     * @since 1.0.4
     */
    void taskMoveBlocks();

    /**
     * place extending head
     *
     * @since 1.0.4
     */
    void taskPlaceExtendingHead();

    /**
     * remove left over blocks
     *
     * @since 1.0.4
     */
    void taskRemoveLeftOverBlocks();

    /**
     * do remove neighbor updates
     *
     * @since 1.0.4
     */
    void taskDoRemoveNeighborUpdates();

    /**
     *
     * do destroy neighbor updates
     * @since 1.0.4
     */
    void taskDoDestroyNeighborUpdates();

    /**
     * do move neighbor updates
     *
     * @since 1.0.4
     */
    void taskDoMoveNeighborUpdates();

    /**
     * do unmerge neighbor updates
     *
     * @since 1.0.4
     */
    void taskDoUnMergeUpdates();

    /**
     * update neighbors at piston head on extending
     *
     * @since 1.0.4
     */
    void taskDoPistonHeadExtendingUpdate();

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

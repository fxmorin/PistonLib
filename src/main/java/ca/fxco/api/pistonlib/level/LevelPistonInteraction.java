package ca.fxco.api.pistonlib.level;

import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * Adds piston events to the level. Allowing for decouple pistons calls without needing pistons.
 *
 * @author FX
 * @since 1.0.4
 */
public interface LevelPistonInteraction {

    /**
     * Add a piston event for the specified piston action.
     *
     * @param pistonBase block to add to event
     * @param pos block position of the block
     * @param dir direction it's being moved
     * @param extend is piston extending
     * @since 1.0.4
     */
    void pl$addPistonEvent(BasicPistonBaseBlock pistonBase, BlockPos pos, Direction dir, boolean extend);

}

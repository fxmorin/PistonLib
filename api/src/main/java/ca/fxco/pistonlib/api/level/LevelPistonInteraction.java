package ca.fxco.pistonlib.api.level;

import ca.fxco.pistonlib.api.block.PLPistonController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;

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
     * @param pistonBase piston base block which can provide a piston controller
     * @param pos block position of the block
     * @param dir direction it's being moved
     * @param extend is piston extending
     * @since 1.0.4
     */
    <P extends Block & PLPistonController> void pl$addPistonEvent(P pistonBase, BlockPos pos,
                                                                  Direction dir, boolean extend);

}

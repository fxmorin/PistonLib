package ca.fxco.pistonlib.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * PistonTicking
 * <p>
 * Allows blocks to run code every tick while being moved by a piston.
 * Can be used on Blocks and BlockEntities.
 * The {@code PistonLibConfig.tickingApi} must be enabled to use any of the features provided here
 *
 * @author FX
 * @since 1.0.4
 */
public interface MovingTickable {

    /**
     * Runs a moving tick on the block or block entity, while its being moving by a piston.
     *
     * @param level    level of the block
     * @param state    block state of the block
     * @param toPos    block position its moving to
     * @param dir      the direction it's moving in
     * @param progress of the move
     * @param speed    of the move
     * @param merging  if it's merging
     * @since 1.0.4
     */
    void pl$movingTick(Level level, BlockState state, BlockPos toPos, Direction dir, float progress,
                       float speed, boolean merging);

}

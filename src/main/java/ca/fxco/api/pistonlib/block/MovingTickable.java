package ca.fxco.api.pistonlib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * PistonTicking
 *
 * Allows blocks to run code every tick while being moved by a piston
 * Can be used on Blocks and BlockEntities
 * The `tickingApi` config option must be enabled in order to use any of the features provided here
 * @author Space Walker
 *
 */
public interface MovingTickable {

    /**
     * @param level level of the block
     * @param state block state of the block
     * @param toPos block pos its moving to
     * @param dir direction it's moving in
     * @param progress of the move
     * @param speed of the move
     * @param merging is it merging
     * @since 1.0.4
     */
    void pl$movingTick(Level level, BlockState state, BlockPos toPos, Direction dir, float progress, float speed, boolean merging);

}

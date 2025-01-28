package ca.fxco.api.pistonlib.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Interface defining mechanics related to entities interacting with pistons in the game world.
 * This interface provides methods to customize how entities behave when pushed by pistons
 * and interact with blocks during such events.
 *
 * @author FX
 * @since 1.2.0
 */
public interface EntityPistonMechanics {

    /**
     * Determines whether the entity can trigger API calls related to being pushed into blocks by a piston.
     * If this returns {@code false}, the entity doesn't invoke checks or events related to being pushed
     * into blocks. This doesn't affect the entity's physical behavior or movement, only the associated API calls.
     *
     * @return {@code true} if the entity can trigger block-push-related API calls, otherwise {@code false}.
     */
    default boolean pl$canPushIntoBlocks() {
        return false;
    }

    /**
     * Called when the entity is pushed into a block, provided {@link #pl$canPushIntoBlocks()} returns {@code true}.
     * This method determines whether the block should perform its own checks against the entity.
     *
     * @param state the {@link BlockState} of the block the entity is being pushed into.
     * @param pos   the {@link BlockPos} of the block the entity is being pushed into.
     * @return {@code true} if the block should perform its own checks against the entity, otherwise {@code false}.
     */
    default boolean pl$onPushedIntoBlock(BlockState state, BlockPos pos) {
        return true;
    }

    /**
     * Called when the entity is being pushed by a piston and comes into contact with a block.
     * <p>
     * This method runs regardless of the return value of {@link #pl$canPushIntoBlocks()}. However:
     * <ul>
     *   <li>If {@link #pl$canPushIntoBlocks()} returns {@code false}, the {@code crushedAgainst} parameter
     *       is always be {@code null}.</li>
     *   <li>If the entity is being crushed against multiple blocks simultaneously, {@code crushedAgainst}
     *       is also {@code null}.</li>
     * </ul>
     *
     * @param crushedAgainst the {@link BlockState} of the block the entity is being crushed against,
     *                       or {@code null} if no specific block can be determined.
     */
    default void pl$onPistonCrushing(@Nullable BlockState crushedAgainst) {}
}

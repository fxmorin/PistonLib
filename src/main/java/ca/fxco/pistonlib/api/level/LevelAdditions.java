package ca.fxco.pistonlib.api.level;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * An interface added to Level, in order to implement post load calls for block entities.
 *
 * @author FX
 * @since 1.0.4
 */
public interface LevelAdditions {

    /**
     * Adds block entity to the post load.
     *
     * @param blockEntity block entity to add
     * @since 1.0.4
     */
    void pl$addBlockEntityPostLoad(BlockEntity blockEntity);

}

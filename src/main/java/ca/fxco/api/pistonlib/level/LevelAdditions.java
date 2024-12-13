package ca.fxco.api.pistonlib.level;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author FX
 * @since 1.0.4
 */
public interface LevelAdditions {

    /**
     * adds block entity to the post load
     * @param blockEntity block entity to add
     * @since 1.0.4
     */
    void pl$addBlockEntityPostLoad(BlockEntity blockEntity);

}

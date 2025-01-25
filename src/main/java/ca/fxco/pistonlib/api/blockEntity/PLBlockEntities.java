package ca.fxco.pistonlib.api.blockEntity;

import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Helper methods for registering custom moving block entity types.
 *
 * @author Space Walker
 * @since 1.0.4
 */
public class PLBlockEntities {

    /**
     * Builds a block entity type from the given factories and
     * registers it to the given namespaced id.
     *
     * @param <T>      the type of moving block entity
     * @param id       a namespaced id to uniquely identify the block
     *                 entity type
     * @param factory  the block entity factory for the block
     *                 entity registry
     * @param families the piston families to register the moving
     *                 block entity type to
     * @return the block entity type that was registered
     */
    public static <T extends BasicMovingBlockEntity> BlockEntityType<T> register(
            ResourceLocation id,
            FabricBlockEntityTypeBuilder.Factory<T> factory
    ) {
        BlockEntityType<T> type = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                id,
                FabricBlockEntityTypeBuilder.create(factory).build(null)
        );

        return type;
    }
}

package ca.fxco.api.pistonlib.blockEntity;

import ca.fxco.api.pistonlib.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Helper methods for registering custom moving block entity types.
 * @author Space Walker
 * @since 1.0.4
 */
public class PLBlockEntities {

    /**
     * Builds a block entity type from the given factories and
     * registers it to the given namespaced id.
     * @param <T> the type of moving block entity
     * @param id a namespaced id to uniquely identify the block
     *           entity type
     * @param factory1 the block entity factory for the block
     *                 entity registry
     * @param factory2 the block entity factory for the piston
     *                 families
     * @param families the piston families to register the moving
     *                 block entity type to
     * @return the block entity type that was registered
     */
    public static <T extends BasicMovingBlockEntity> BlockEntityType<T> register(
            ResourceLocation id,
            FabricBlockEntityTypeBuilder.Factory<T> factory1,
            BasicMovingBlockEntity.Factory<T> factory2,
            PistonFamily... families
    ) {
        BlockEntityType<T> type = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                id,
                FabricBlockEntityTypeBuilder.create(
                        factory1,
                        Util.make(new Block[families.length], blocks -> {
                            for (int i = 0; i < families.length; i++) {
                                blocks[i] = families[i].getMoving();
                            }
                        })
                ).build(null)
        );

        for (PistonFamily family : families) {
            family.setMovingBlockEntity(type, factory2);
        }

        return type;
    }
}

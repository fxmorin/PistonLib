package ca.fxco.pistonlib.datagen;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.api.PistonLibRegistries;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.base.ModBlocks;
import ca.fxco.pistonlib.base.ModPistonFamilies;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public static final Logger LOGGER = PistonLib.LOGGER;

    protected ModBlockLootTableProvider(FabricDataOutput dataOutput,
                                        CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        LOGGER.info("Generating block loot tables...");

        for (Map.Entry<ResourceKey<PistonFamily>, PistonFamily> entry : PistonLibRegistries.PISTON_FAMILY.entrySet()) {
            ResourceKey<PistonFamily> key = entry.getKey();
            PistonFamily family = entry.getValue();

            if (family == ModPistonFamilies.VANILLA) {
                continue;
            }

            LOGGER.info("Generating block loot tables for piston family " + key.location() + "...");

            family.getBases().forEach((type, base) -> {
                dropSelf(base);
            });
        }

        LOGGER.info("Finished generating block loot tables for pistons, generating for other blocks...");

        dropSelf(ModBlocks.HALF_SLIME_BLOCK);
        dropSelf(ModBlocks.HALF_HONEY_BLOCK);
        dropSelf(ModBlocks.HALF_REDSTONE_BLOCK);
        dropSelf(ModBlocks.HALF_OBSIDIAN_BLOCK);
        dropSelf(ModBlocks.HALF_REDSTONE_LAMP_BLOCK);

        dropSelf(ModBlocks.DRAG_BLOCK);
        dropSelf(ModBlocks.STICKYLESS_BLOCK);
        dropSelf(ModBlocks.STICKY_TOP_BLOCK);
        dropSelf(ModBlocks.SLIMY_REDSTONE_BLOCK);
        dropSelf(ModBlocks.ALL_SIDED_OBSERVER);
        dropSelf(ModBlocks.GLUE_BLOCK);
        dropSelf(ModBlocks.POWERED_STICKY_BLOCK);
        dropSelf(ModBlocks.STICKY_CHAIN_BLOCK);
        dropSelf(ModBlocks.AXIS_LOCKED_BLOCK);
        dropSelf(ModBlocks.MOVE_COUNTING_BLOCK);
        dropSelf(ModBlocks.OBSIDIAN_SLAB_BLOCK);
        dropSelf(ModBlocks.OBSIDIAN_STAIR_BLOCK);
        dropSelf(ModBlocks.WEAK_REDSTONE_BLOCK);
        dropSelf(ModBlocks.AUTO_CRAFTING_BLOCK);
		dropSelf(ModBlocks.QUASI_BLOCK);
        dropSelf(ModBlocks.ERASE_BLOCK);
		dropSelf(ModBlocks.HEAVY_BLOCK);

        dropSelf(ModBlocks.SLIPPERY_SLIME_BLOCK);
        dropSelf(ModBlocks.SLIPPERY_REDSTONE_BLOCK);
        dropSelf(ModBlocks.SLIPPERY_STONE_BLOCK);

        LOGGER.info("Finished generating block loot tables!");
    }
}

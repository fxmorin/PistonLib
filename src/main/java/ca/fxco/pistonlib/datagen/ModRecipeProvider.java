package ca.fxco.pistonlib.datagen;

import ca.fxco.api.pistonlib.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.base.ModBlocks;
import ca.fxco.pistonlib.base.ModPistonFamilies;
import ca.fxco.pistonlib.base.ModRegistries;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.PistonType;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public static final Logger LOGGER = PistonLib.LOGGER;

	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, registryLookup);
	}

	@Override
	public void buildRecipes(RecipeOutput exporter) {
		LOGGER.info("Generating recipes...");

		for (Map.Entry<ResourceKey<PistonFamily>, PistonFamily> entry : ModRegistries.PISTON_FAMILY.entrySet()) {
            ResourceKey<PistonFamily> key = entry.getKey();
            PistonFamily family = entry.getValue();

			if (family == ModPistonFamilies.VANILLA) {
				continue;
			}

            LOGGER.info("Generating recipes for piston family "+key.location()+"...");

            Block normalBase = family.getBase(PistonType.DEFAULT);
            Block stickyBase = family.getBase(PistonType.STICKY);

            if (normalBase != null && stickyBase != null && normalBase.asItem() != Items.AIR &&
					stickyBase.asItem() != Items.AIR) {
                offerStickyPistonRecipe(exporter, stickyBase, normalBase);
            }
        }

		LOGGER.info("Finished generating recipes for pistons, generating for other items...");

		offerSlipperyBlockRecipe(exporter, ModBlocks.SLIPPERY_REDSTONE_BLOCK, Blocks.REDSTONE_BLOCK);
		offerSlipperyBlockRecipe(exporter, ModBlocks.SLIPPERY_SLIME_BLOCK, Blocks.SLIME_BLOCK);
		offerSlipperyBlockRecipe(exporter, ModBlocks.SLIPPERY_STONE_BLOCK, Blocks.STONE);

		generateRecipes(exporter, new BlockFamily.Builder(Blocks.OBSIDIAN).slab(ModBlocks.OBSIDIAN_SLAB_BLOCK).stairs(ModBlocks.OBSIDIAN_STAIR_BLOCK).getFamily(), FeatureFlags.VANILLA_SET);

		LOGGER.info("Finished generating recipes!");
	}

	public void offerSlipperyBlockRecipe(RecipeOutput exporter, Block slipperyBlock, Block baseBlock) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, slipperyBlock, 1).requires(baseBlock).requires(Items.POTION).unlockedBy(getHasName(baseBlock), has(baseBlock)).save(exporter);
	}

	public void offerStickyPistonRecipe(RecipeOutput exporter, Block stickyPiston, Block regularPiston) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, stickyPiston).define('P', regularPiston).define('S', Items.SLIME_BALL).pattern("S").pattern("P").unlockedBy("has_slime_ball", has(Items.SLIME_BALL)).save(exporter);
	}
}

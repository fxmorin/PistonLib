package ca.fxco.pistonlib.datagen;

import ca.fxco.pistonlib.recipes.pistonCrushing.SingleCrushingConditionalRecipe;
import ca.fxco.pistonlib.recipes.pistonCrushing.builders.MultiCrushingRecipeBuilder;
import ca.fxco.pistonlib.recipes.pistonCrushing.builders.PairCrushingRecipeBuilder;
import ca.fxco.pistonlib.recipes.pistonCrushing.builders.SingleCrushingRecipeBuilder;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.api.PistonLibRegistries;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.base.ModBlocks;
import ca.fxco.pistonlib.base.ModPistonFamilies;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.PistonType;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

	public static final Logger LOGGER = PistonLib.LOGGER;

	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(output, registryLookup);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				LOGGER.info("Generating recipes...");

				var itemRegistry = this.registries.lookupOrThrow(Registries.ITEM);

				for (var entry : PistonLibRegistries.PISTON_FAMILY.entrySet()) {
					ResourceKey<PistonFamily> key = entry.getKey();
					PistonFamily family = entry.getValue();

					if (family == ModPistonFamilies.VANILLA) {
						continue;
					}

					LOGGER.info("Generating recipes for piston family " + key.location() + "...");

					Block normalBase = family.getBase(PistonType.DEFAULT);
					Block stickyBase = family.getBase(PistonType.STICKY);

					if (normalBase != null && stickyBase != null && normalBase.asItem() != Items.AIR &&
							stickyBase.asItem() != Items.AIR) {
						ShapedRecipeBuilder.shaped(itemRegistry, RecipeCategory.REDSTONE, stickyBase)
								.define('P', normalBase)
								.define('S', Items.SLIME_BALL)
								.pattern("S")
								.pattern("P")
								.unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
								.save(exporter);
					}
				}

				LOGGER.info("Finished generating recipes for pistons, generating for other items...");

				BlockFamily obsidianFamily = new BlockFamily.Builder(Blocks.OBSIDIAN)
						.slab(ModBlocks.OBSIDIAN_SLAB_BLOCK)
						.stairs(ModBlocks.OBSIDIAN_STAIR_BLOCK)
						.getFamily();
				generateRecipes(obsidianFamily, FeatureFlags.VANILLA_SET);

				Map<Block, Block> slipperyBlockRecipes = Map.of(
						ModBlocks.SLIPPERY_REDSTONE_BLOCK, Blocks.REDSTONE_BLOCK,
						ModBlocks.SLIPPERY_SLIME_BLOCK, Blocks.SLIME_BLOCK,
						ModBlocks.SLIPPERY_STONE_BLOCK, Blocks.STONE
				);
				for (Map.Entry<Block, Block> entry : slipperyBlockRecipes.entrySet()) {
					Block baseBlock = entry.getValue();
					ShapelessRecipeBuilder.shapeless(itemRegistry, RecipeCategory.MISC, entry.getKey(), 1)
							.requires(baseBlock)
							.requires(Items.POTION)
							.unlockedBy(getHasName(baseBlock), has(baseBlock))
							.save(exporter);
				}

				Map<Block, Item> simpleCrushingRecipes = Map.of(
						Blocks.IRON_ORE, Items.RAW_IRON,
						Blocks.COPPER_ORE, Items.RAW_COPPER,
						Blocks.GOLD_ORE, Items.RAW_GOLD
				);
				for (Map.Entry<Block, Item> entry : simpleCrushingRecipes.entrySet()) {
					SingleCrushingRecipeBuilder.crushing(entry.getKey(), entry.getValue()).save(exporter);
				}

				Map<Block, Item> highPressureCrushingRecipes = Map.of(
						Blocks.STONE_BRICKS, Items.CRACKED_STONE_BRICKS,
						Blocks.INFESTED_STONE_BRICKS, Items.INFESTED_CRACKED_STONE_BRICKS,
						Blocks.DEEPSLATE_BRICKS, Items.CRACKED_DEEPSLATE_BRICKS,
						Blocks.DEEPSLATE_TILES, Items.CRACKED_DEEPSLATE_TILES,
						Blocks.NETHER_BRICKS, Items.CRACKED_NETHER_BRICKS,
						Blocks.POLISHED_BLACKSTONE_BRICKS, Items.CRACKED_POLISHED_BLACKSTONE_BRICKS
				);
				for (Map.Entry<Block, Item> entry : highPressureCrushingRecipes.entrySet()) {
					SingleCrushingRecipeBuilder.crushing(entry.getKey(), entry.getValue())
							.hasConditional(SingleCrushingConditionalRecipe.Condition.HIGHER_RESISTANCE, 1199F)
							.save(exporter);
				}

				ItemStack sticks = new ItemStack(Items.STICK, 4);
				PairCrushingRecipeBuilder.crushing(Blocks.OAK_PLANKS, Blocks.OAK_PLANKS, sticks).save(exporter);

				MultiCrushingRecipeBuilder.crushingItems(
						List.of(Blocks.STONE, Items.QUARTZ, Blocks.ANDESITE),
						Blocks.DIORITE.asItem(),
						3
				).save(exporter);

				LOGGER.info("Finished generating recipes!");
			}
		};
	}

	@Override
	public String getName() {
		return "PistonLibRecipeProvider";
	}
}

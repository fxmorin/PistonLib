package ca.fxco.pistonlib.datagen;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.recipes.pistonCrushing.SingleCrushingConditionalRecipe;
import ca.fxco.pistonlib.recipes.pistonCrushing.builders.MultiCrushingRecipeBuilder;
import ca.fxco.pistonlib.recipes.pistonCrushing.builders.PairCrushingRecipeBuilder;
import ca.fxco.pistonlib.recipes.pistonCrushing.builders.SingleCrushingRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

				ItemStack planks = new ItemStack(Items.OAK_PLANKS, 2);
				SingleCrushingRecipeBuilder.crushing(planks, sticks).save(exporter, "pistonlib:stick_2");

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

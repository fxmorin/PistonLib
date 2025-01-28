package ca.fxco.pistonlib.datagen;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.base.ModPistonFamilies;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

import static net.minecraft.client.data.models.BlockModelGenerators.createSlab;

public class ModModelProvider extends FabricModelProvider {

    public static final Logger LOGGER = PistonLib.LOGGER;

	public static final ModelTemplate TEMPLATE_PISTON_ARM = new ModelTemplate(Optional.of(PistonLib.id("block/template_piston_arm")), Optional.empty(), TextureSlot.TEXTURE);
	public static final ModelTemplate TEMPLATE_PISTON_ARM_SHORT = new ModelTemplate(Optional.of(PistonLib.id("block/template_piston_arm_short")), Optional.empty(), TextureSlot.TEXTURE);
	public static final ModelTemplate TEMPLATE_PARTICLE_ONLY = new ModelTemplate(Optional.of(PistonLib.id("block/template_empty")), Optional.empty(), TextureSlot.PARTICLE);
	public static final ModelTemplate TEMPLATE_HALF_BLOCK = new ModelTemplate(Optional.of(PistonLib.id("block/template_half_block")), Optional.empty(), TextureSlot.TOP, TextureSlot.SIDE);
	public static final ModelTemplate PISTON_BASE = new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("block/piston_extended")), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.INSIDE);

	public ModModelProvider(FabricDataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators generator) {
		LOGGER.info("Generating blockstate definitions and models...");

		registerPistonFamily(generator, ModPistonFamilies.BASIC);

		TextureMapping particleOnlyTextureMap = new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
		createTrivialBlock(ca.fxco.pistonlib.base.ModBlocks.MERGE_BLOCK, particleOnlyTextureMap, TEMPLATE_PARTICLE_ONLY, generator);

		LOGGER.info("Finished generating blockstate definitions and models!");
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerators) {

	}

	public static void createTrivialBlock(Block block, TextureMapping textureMapping, ModelTemplate modelTemplate, BlockModelGenerators generators) {
		ResourceLocation resourceLocation = modelTemplate.create(block, textureMapping, generators.modelOutput);
		generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, resourceLocation));
	}

	public static void registerCubeTextureMap(BlockModelGenerators generator, Block block,
											  ResourceLocation baseTexture, @Nullable String suffix) {
		TextureMapping halfBlockTextureMap = new TextureMapping().put(TextureSlot.ALL, baseTexture);
		if (suffix == null) {
			ModelTemplates.CUBE_ALL.create(block, halfBlockTextureMap, generator.modelOutput);
		} else {
			ModelTemplates.CUBE_ALL.createWithSuffix(block, suffix, halfBlockTextureMap, generator.modelOutput);
		}
	}

	public static void registerHalfBlockTextureMap(BlockModelGenerators generator, Block halfBlock, ResourceLocation baseTexture) {
		registerHalfBlockTextureMap(generator, halfBlock, baseTexture, null);
	}

	public static void registerHalfBlockTextureMap(BlockModelGenerators generator, Block halfBlock, ResourceLocation baseTexture, @Nullable String suffix) {
		TextureMapping halfBlockTextureMap = new TextureMapping().put(TextureSlot.SIDE, baseTexture).put(TextureSlot.TOP, baseTexture);
		if (suffix == null) {
			TEMPLATE_HALF_BLOCK.create(halfBlock, halfBlockTextureMap, generator.modelOutput);
		} else {
			TEMPLATE_HALF_BLOCK.createWithSuffix(halfBlock, suffix, halfBlockTextureMap, generator.modelOutput);
		}
	}

	public static void registerBlockWithCustomModel(BlockModelGenerators generator, Block halfBlock) {
		registerHalfBlock(generator, halfBlock, null);
	}

	public static void registerBlockWithCustomStates(BlockModelGenerators generator, Block halfBlock, PropertyDispatch customStates) {
		generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(halfBlock, Variant.variant()
				.with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(halfBlock))).with(customStates)
		);
	}

	private static void registerSlab(BlockModelGenerators generator, Block baseBlock, Block block) {
		TextureMapping textureBase = TextureMapping.cube(baseBlock);
		ResourceLocation bottom = ModelTemplates.SLAB_BOTTOM.create(block, textureBase, generator.modelOutput);
		ResourceLocation top = ModelTemplates.SLAB_TOP.create(block, textureBase, generator.modelOutput);
		ResourceLocation _double = ModelTemplates.CUBE_COLUMN.createWithOverride(block, "_double", textureBase, generator.modelOutput);
		generator.blockStateOutput.accept(createSlab(block, bottom, top, _double));
		generator.registerSimpleItemModel(block, bottom);
	}

	private static void registerStair(BlockModelGenerators generator, Block baseBlock, Block block) {
		TextureMapping textureBase = TextureMapping.cube(baseBlock);
		ResourceLocation inner = ModelTemplates.STAIRS_INNER.create(block, textureBase, generator.modelOutput);
		ResourceLocation flat = ModelTemplates.STAIRS_STRAIGHT.create(block, textureBase, generator.modelOutput);
		ResourceLocation outer = ModelTemplates.STAIRS_OUTER.create(block, textureBase, generator.modelOutput);
		generator.blockStateOutput.accept(BlockModelGenerators.createStairs(block, inner, flat, outer));
		generator.registerSimpleItemModel(block, flat);
	}

	public static void registerHalfBlock(BlockModelGenerators generator, Block halfBlock, @Nullable Block base) {
		generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(halfBlock, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(halfBlock))).with(generator.createColumnWithFacing()));

		if (base != null) {
			ResourceLocation baseTextureId = TextureMapping.getBlockTexture(base);

			TextureMapping halfBlockTextureMap = new TextureMapping().put(TextureSlot.SIDE, baseTextureId).put(TextureSlot.TOP, baseTextureId);

			TEMPLATE_HALF_BLOCK.create(halfBlock, halfBlockTextureMap, generator.modelOutput);
		}
	}

	public static void registerPistonFamily(BlockModelGenerators generator, PistonFamily family) {
		boolean customTextures = family.hasCustomTextures();
		Block textureBaseBlock = customTextures ? family.getBase() : Blocks.PISTON;

		Block base = family.getBase();
		Block normalBase = family.getBase(PistonType.DEFAULT);
		Block stickyBase = family.getBase(PistonType.STICKY);
		Block arm = family.getArm();
		Block head = family.getHead();
		Block moving = family.getMoving();

		ResourceLocation sideTextureId = TextureMapping.getBlockTexture(textureBaseBlock, "_side");

		TextureMapping textureMap = new TextureMapping().put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(textureBaseBlock, "_bottom")).put(TextureSlot.SIDE, sideTextureId);

		TextureMapping baseTextureMap = textureMap.copyAndUpdate(TextureSlot.INSIDE, TextureMapping.getBlockTexture(textureBaseBlock, "_inner"));

		ResourceLocation baseModelId = PISTON_BASE.createWithSuffix(base, "_base", baseTextureMap, generator.modelOutput);

		ResourceLocation topRegularTextureId = TextureMapping.getBlockTexture(textureBaseBlock, "_top");
		ResourceLocation topStickyTextureId = TextureMapping.getBlockTexture(textureBaseBlock, "_top_sticky");

		if (normalBase != null) {
			TextureMapping regularTextureMap = textureMap.copyAndUpdate(TextureSlot.PLATFORM, topRegularTextureId);
			generator.createPistonVariant(normalBase, baseModelId, regularTextureMap);
			ResourceLocation regularInventoryModelId = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(normalBase, "_inventory", textureMap.copyAndUpdate(TextureSlot.TOP, topRegularTextureId), generator.modelOutput);
			if (normalBase.asItem() != Items.AIR) generator.registerSimpleItemModel(normalBase, regularInventoryModelId);
		}

		if (stickyBase != null) {
			TextureMapping stickyTextureMap = textureMap.copyAndUpdate(TextureSlot.PLATFORM, topStickyTextureId);
			generator.createPistonVariant(stickyBase, baseModelId, stickyTextureMap);
			ResourceLocation stickyInventoryModelId = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(stickyBase, "_inventory", textureMap.copyAndUpdate(TextureSlot.TOP, topStickyTextureId), generator.modelOutput);
			if (stickyBase.asItem() != Items.AIR) generator.registerSimpleItemModel(stickyBase, stickyInventoryModelId);
		}

		if (head != null) {
			TextureMapping baseHeadTextureMap = new TextureMapping().put(TextureSlot.UNSTICKY, topRegularTextureId).put(TextureSlot.SIDE, sideTextureId);
			TextureMapping regularHeadTextureMap = baseHeadTextureMap.copyAndUpdate(TextureSlot.PLATFORM, topRegularTextureId);
			TextureMapping stickyHeadTextureMap = baseHeadTextureMap.copyAndUpdate(TextureSlot.PLATFORM, topStickyTextureId);

			generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(head).with(
					PropertyDispatch.properties(BlockStateProperties.SHORT, BlockStateProperties.PISTON_TYPE)
							.select(false, PistonType.DEFAULT, Variant.variant().with(
									VariantProperties.MODEL, ModelTemplates.PISTON_HEAD.createWithSuffix(base, "_head", regularHeadTextureMap, generator.modelOutput)))
							.select(false, PistonType.STICKY, Variant.variant().with(
									VariantProperties.MODEL, ModelTemplates.PISTON_HEAD.createWithSuffix(base, "_head_sticky", stickyHeadTextureMap, generator.modelOutput)))
							.select(true, PistonType.DEFAULT, Variant.variant().with(
									VariantProperties.MODEL, ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(base, "_head_short", regularHeadTextureMap, generator.modelOutput)))
							.select(true, PistonType.STICKY, Variant.variant().with(
									VariantProperties.MODEL, ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(base, "_head_short_sticky", stickyHeadTextureMap, generator.modelOutput)))
			).with(BlockModelGenerators.createFacingDispatch()));
		}

		if (arm != null) {
			TextureMapping armTextureMap = new TextureMapping().put(TextureSlot.TEXTURE, sideTextureId);

			generator.blockStateOutput.accept((MultiVariantGenerator.multiVariant(arm).with(
					PropertyDispatch.property(BlockStateProperties.SHORT)
							.select(false, Variant.variant().with(
									VariantProperties.MODEL, TEMPLATE_PISTON_ARM.createWithSuffix(base, "_arm", armTextureMap, generator.modelOutput)))
							.select(true, Variant.variant().with(
									VariantProperties.MODEL, TEMPLATE_PISTON_ARM_SHORT.createWithSuffix(base, "_arm_short", armTextureMap, generator.modelOutput)))
			).with(BlockModelGenerators.createFacingDispatch())));
		}

		if (moving != null) {
			TextureMapping movingPistonTextureMap = new TextureMapping().put(TextureSlot.PARTICLE, sideTextureId);

			createTrivialBlock(moving, movingPistonTextureMap, TEMPLATE_PARTICLE_ONLY, generator);
		}
	}

	public static void registerPoweredBlock(BlockModelGenerators generator, Block block) {
		ResourceLocation powerOff = ModelLocationUtils.getModelLocation(block);
		ResourceLocation powerOn = ModelLocationUtils.getModelLocation(block, "_on");
		registerBlockWithCustomStates(generator, block,
				PropertyDispatch.property(BlockStateProperties.POWERED)
						.select(false, Variant.variant().with(VariantProperties.MODEL, powerOff))
						.select(true, Variant.variant().with(VariantProperties.MODEL, powerOn)));
		registerCubeTextureMap(generator, block, powerOff, null);
		registerCubeTextureMap(generator, block, powerOn, "_on");
	}

	public static PropertyDispatch createLitFacingBlockState(ResourceLocation offModelId, ResourceLocation onModelId) {
		return PropertyDispatch
				.properties(BlockStateProperties.FACING, BlockStateProperties.LIT)
				.select(Direction.NORTH, false,
						Variant.variant()
								.with(VariantProperties.MODEL, offModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
				.select(Direction.SOUTH, false,
						Variant.variant()
								.with(VariantProperties.MODEL, offModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.EAST, false,
						Variant.variant()
								.with(VariantProperties.MODEL, offModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, false,
						Variant.variant()
								.with(VariantProperties.MODEL, offModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.DOWN, false,
						Variant.variant()
								.with(VariantProperties.MODEL, offModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
				.select(Direction.UP, false,
						Variant.variant()
								.with(VariantProperties.MODEL, offModelId))
				.select(Direction.NORTH, true,
						Variant.variant()
								.with(VariantProperties.MODEL, onModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
				.select(Direction.SOUTH, true,
						Variant.variant()
								.with(VariantProperties.MODEL, onModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(Direction.EAST, true,
						Variant.variant()
								.with(VariantProperties.MODEL, onModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(Direction.WEST, true,
						Variant.variant()
								.with(VariantProperties.MODEL, onModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
								.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(Direction.DOWN, true,
						Variant.variant()
								.with(VariantProperties.MODEL, onModelId)
								.with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
				.select(Direction.UP, true,
						Variant.variant()
								.with(VariantProperties.MODEL, onModelId));
	}
}

package ca.fxco.pistonlib.commands;

import ca.fxco.api.pistonlib.pistonLogic.PistonMoveBehavior;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.base.ModBlocks;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import ca.fxco.pistonlib.commands.arguments.DirectionArgument;
import ca.fxco.pistonlib.commands.arguments.PistonMoveBehaviorArgument;
import ca.fxco.pistonlib.helpers.BlockUtils;
import ca.fxco.pistonlib.helpers.PistonLibBehaviorManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.*;

public class PistonLibCommand implements Command {

    private static final DynamicCommandExceptionType CANNOT_CHANGE_MOVE_BEHAVIOR = new DynamicCommandExceptionType(
            o -> Component.translatable("commands.pistonlib.behavior.illegalChange", o));

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher,
                         CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("pistonlib")
                .requires(source -> source.hasPermission(2))
                .then(pistonEventSubCommand(registryAccess, PistonEventType.PUSH)) // Push Command
                .then(pistonEventSubCommand(registryAccess, PistonEventType.PULL)) // Pull Command
                .then(addOptionArgs(Commands.literal("config")
                        .requires(source -> source.hasPermission(4))))
                .then(Commands.literal("behavior").requires(source -> source.hasPermission(4))
                        .then(Commands.argument("block", BlockStateArgument.block(registryAccess))
                                .executes(context -> queryBehavior(
                                        context.getSource(), BlockStateArgument.getBlock(context, "block")
                                ))
                                .then(Commands.literal("default")
                                        .executes(ctx -> setBehaviorOverride(
                                                ctx.getSource(),
                                                BlockStateArgument.getBlock(ctx, "block"),
                                                PistonMoveBehavior.DEFAULT
                                        ))
                                )
                                .then(Commands.
                                        argument("behavior", PistonMoveBehaviorArgument.pistonMoveBehavior()).
                                        executes(context -> setBehaviorOverride(
                                                context.getSource(),
                                                BlockStateArgument.getBlock(context, "block"),
                                                PistonMoveBehaviorArgument.getPistonMoveBehavior(context, "behavior")
                                        ))
                                )
                        )
                )
        );
    }

    private LiteralArgumentBuilder<CommandSourceStack> pistonEventSubCommand(CommandBuildContext registryAccess,
                                                                             PistonEventType eventType) {
        return Commands.literal(eventType.name().toLowerCase())
            .executes(ctx ->
                runPistonEvent(
                    ctx.getSource(),
                    null,
                    null,
                    null,
                    eventType
                )
            )
            .then(Commands.argument("pos", BlockPosArgument.blockPos())
                .then(Commands.argument("towards", DirectionArgument.direction())
                    .executes(ctx ->
                        runPistonEvent(
                            ctx.getSource(),
                            GlobalPos.of(
                                ctx.getSource().getLevel().dimension(),
                                BlockPosArgument.getLoadedBlockPos(ctx, "pos")
                            ),
                            DirectionArgument.getDirection(ctx, "towards"),
                            null,
                            eventType
                        )
                    )
                    .then(Commands.literal("in")
                        .then(Commands.argument("dimension", DimensionArgument.dimension())
                            .executes(ctx ->
                                runPistonEvent(
                                    ctx.getSource(),
                                    GlobalPos.of(
                                        DimensionArgument.getDimension(ctx, "dimension").dimension(),
                                        BlockPosArgument.getLoadedBlockPos(ctx, "pos")
                                    ),
                                    DirectionArgument.getDirection(ctx, "towards"),
                                    null,
                                    eventType
                                )
                            )
                        )
                    )
                    .then(Commands.argument("pistonBlock", BlockStateArgument.block(registryAccess))
                        .executes(ctx ->
                            runPistonEvent(
                                ctx.getSource(),
                                GlobalPos.of(
                                    ctx.getSource().getLevel().dimension(),
                                    BlockPosArgument.getLoadedBlockPos(ctx, "pos")
                                ),
                                DirectionArgument.getDirection(ctx, "towards"),
                                BlockStateArgument.getBlock(ctx, "pistonBlock"),
                                eventType
                            )
                        )
                        .then(Commands.literal("in")
                            .then(Commands.argument("dimension", DimensionArgument.dimension())
                                .executes(ctx ->
                                    runPistonEvent(
                                        ctx.getSource(),
                                        GlobalPos.of(
                                            DimensionArgument.getDimension(ctx, "dimension").dimension(),
                                            BlockPosArgument.getLoadedBlockPos(ctx, "pos")
                                        ),
                                        DirectionArgument.getDirection(ctx, "towards"),
                                        BlockStateArgument.getBlock(ctx, "pistonBlock"),
                                        eventType
                                    )
                                )
                            )
                        )
                    )
                )
            );
    }

    private static int runPistonEvent(CommandSourceStack commandSourceStack, GlobalPos globalPos, Direction facing,
                                      BlockInput blockInput, PistonEventType eventType) throws CommandSyntaxException {
        Block block = blockInput == null ? ModBlocks.BASIC_STICKY_PISTON : blockInput.getState().getBlock();
        if (!(block instanceof BasicPistonBaseBlock basicPistonBaseBlock)) {
            throw new SimpleCommandExceptionType(
                    Component.translatable("commands.pistonlib.notPistonBlock", block)
            ).create();
        }
        BlockPos blockPos;
        ServerLevel serverLevel;
        boolean isPush = eventType == PistonEventType.PUSH;
        if (globalPos == null || facing == null) {
            ServerPlayer player = commandSourceStack.getPlayerOrException();
            HitResult hitResult = player.pick(Minecraft.getInstance().gameMode.getPickRange(), 1.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
                Direction face = blockHitResult.getDirection();
                blockPos = blockHitResult.getBlockPos();
                commandSourceStack.getLevel().pl$addPistonEvent(
                        basicPistonBaseBlock,
                        isPush ? blockPos.relative(face) : blockPos.relative(face, 2),
                        face.getOpposite(),
                        isPush
                );
                commandSourceStack.sendSuccess(() -> Component.translatable(
                        "commands.pistonlib." + eventType.name().toLowerCase() + ".success",
                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        face.getName()
                ), true);
                return 1;
            } else {
                if (globalPos == null) {
                    blockPos = player.blockPosition();
                    serverLevel = commandSourceStack.getLevel();
                } else {
                    blockPos = globalPos.pos();
                    serverLevel = commandSourceStack.getServer().getLevel(globalPos.dimension());
                }
                if (facing == null) {
                    facing = player.getDirection();
                }
            }
        } else {
            blockPos = globalPos.pos();
            serverLevel = commandSourceStack.getServer().getLevel(globalPos.dimension());
        }
        if (serverLevel == null) {
            return 0;
        }
        Direction opposite = facing.getOpposite();
        serverLevel.pl$addPistonEvent(
                basicPistonBaseBlock,
                isPush ? blockPos.relative(opposite) : blockPos,
                opposite,
                isPush
        );
        commandSourceStack.sendSuccess(() -> Component.translatable(
                "commands.pistonlib." + eventType.name().toLowerCase() + ".success",
                blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                opposite.getName()
        ), true);
        return 1;
    }

    private static LiteralArgumentBuilder<CommandSourceStack> addOptionArgs(
            LiteralArgumentBuilder<CommandSourceStack> builder
    ) {
        PistonLib.getConfigManager().getParsedValues().forEach(parsedValue ->
            builder.then(Commands.literal(parsedValue.getName())
                    .executes(ctx -> {
                        ctx.getSource().sendSuccess(() -> Component.translatable("commands.pistonlib.config.value",
                                parsedValue.getName(), parsedValue.getValue()), false);
                        return 1;
                    })
                    .then(Commands.literal("set")
                            .then(Commands.argument("new value", StringArgumentType.word())
                                    .suggests((context, builder1) -> {
                                        Object value = parsedValue.getValue();
                                        String[] suggestions;

                                        if (parsedValue.getSuggestions().length != 0) {
                                            suggestions = parsedValue.getSuggestions();
                                        } else if (value instanceof Boolean) {
                                            suggestions = new String[]{"true", "false"};
                                        } else if (value instanceof Enum<?> enumValue) {
                                            Enum<?>[] enums = enumValue.getClass().getEnumConstants();
                                            suggestions = new String[enums.length];
                                            for (int i = 0; i < enums.length; i++) {
                                                suggestions[i] = enums[i].toString();
                                            }
                                        } else {
                                            return Suggestions.empty();
                                        }
                                        return SharedSuggestionProvider.suggest(suggestions, builder1);
                                    }).executes(ctx -> {
                                        PistonLib.getConfigManager().saveValueFromCommand(parsedValue, ctx.getSource(),
                                                StringArgumentType.getString(ctx, "new value"));
                                        ctx.getSource().sendSuccess(() -> Component.translatable(
                                                "commands.pistonlib.config.success"
                                                        + (parsedValue.requiresRestart() ? ".restart" : ""),
                                                parsedValue.getName(), parsedValue.getValueToSave()), true);
                                        return 1;
                                    })))
                    .then(Commands.literal("default").executes(ctx -> {
                        PistonLib.getConfigManager().resetAndSaveValue(parsedValue);
                        ctx.getSource().sendSuccess(() -> Component.translatable(
                                "commands.pistonlib.config.success"
                                        + (parsedValue.requiresRestart() ? ".restart" : ""),
                                parsedValue.getName(), parsedValue.getValueToSave()), true);
                        return 1;
                    }))
            )
        );
        return builder;
    }

    private static int queryBehavior(CommandSourceStack source, BlockInput input) {
        BlockState state = input.getState();
        PushReaction pushReaction = state.getPistonPushReaction();
        PistonMoveBehavior behavior = PistonMoveBehavior.fromPushReaction(pushReaction);
        PistonMoveBehavior override = PistonLibBehaviorManager.getOverride(state);

        MutableComponent message = Component.
                literal("block state ").
                append(Component.
                        literal(BlockUtils.blockStateAsString(state)).
                        withStyle(ChatFormatting.YELLOW)).
                append(" has piston move behavior ").
                append(Component.
                        literal(behavior.getName()).
                        append(" (").
                        append(override.isPresent() ? "modified" : "vanilla").
                        append(")").
                        withStyle(override.isPresent() ?
                                ChatFormatting.GOLD : ChatFormatting.GREEN, ChatFormatting.BOLD));
        source.sendSuccess(() -> message, false);

        return 1;
    }

    private static int setBehaviorOverride(CommandSourceStack source, BlockInput input, PistonMoveBehavior override)
            throws CommandSyntaxException {
        BlockState state = input.getState();
        Collection<Property<?>> properties = input.getDefinedProperties();
        Collection<BlockState> states = collectMatchingBlockStates(state, properties);

        for (BlockState blockState : states) {
            PistonLibBehaviorManager.setOverride(blockState, override);
        }
        PistonLibBehaviorManager.save(true);

        String stateString = BlockUtils.blockStateAsString(state, properties);

        MutableComponent message = Component.
                literal("set the ").
                append("piston move behavior override of all block states matching ").
                append(Component.
                        literal(stateString).
                        withStyle(ChatFormatting.YELLOW)).
                append(" to ").
                append(Component.
                        literal(override.getName()).
                        withStyle(override == PistonMoveBehavior.DEFAULT ?
                                ChatFormatting.GREEN : ChatFormatting.GOLD, ChatFormatting.BOLD));

        source.sendSuccess(() -> message, true);

        return 1;
    }

    private static Collection<BlockState> collectMatchingBlockStates(
            BlockState state, Collection<Property<?>> properties
    ) throws CommandSyntaxException {
        Collection<BlockState> states = new LinkedList<>();

        for (BlockState blockState : state.getBlock().getStateDefinition().getPossibleStates()) {
            if (blockStatesMatchProperties(state, blockState, properties)) {
                if (!PistonLibBehaviorManager.canChangeOverride(blockState)) {
                    throw CANNOT_CHANGE_MOVE_BEHAVIOR.create(BlockUtils.blockStateAsString(blockState));
                }

                states.add(blockState);
            }
        }

        return states;
    }

    private static boolean blockStatesMatchProperties(BlockState state1, BlockState state2,
                                                      Collection<Property<?>> properties) {
        for (Property<?> property : properties) {
            if (state1.getValue(property) != state2.getValue(property)) {
                return false;
            }
        }

        return true;
    }

    public enum PistonEventType {
        PUSH,
        PULL
    }
}

package ca.fxco.pistonlib.commands.arguments;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.config.ParsedValue;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ParsedValueArgument implements ArgumentType<ParsedValue<?>> {
    private final Collection<ParsedValue<?>> values;

    ParsedValueArgument() {
        values = PistonLib.getConfigManager().getParsedValues();
    }

    public static ParsedValueArgument parsedValue() {
        return new ParsedValueArgument();
    }

    public static ParsedValue<?> getParsedValue(CommandContext<CommandSourceStack> commandContext, String string) {
        return commandContext.getArgument(string, ParsedValue.class);
    }

    @Override
    public ParsedValue<?> parse(StringReader reader) throws CommandSyntaxException {
        return PistonLib.getConfigManager().getParsedValue(reader.readString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        values.forEach(parsedValue -> builder.suggest(parsedValue.getName()));
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}

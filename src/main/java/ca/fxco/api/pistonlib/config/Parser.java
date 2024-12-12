package ca.fxco.api.pistonlib.config;

import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

/**
 * will make sure that the config value is valid, and will convert string inputs to a valid type.
 * used to parse custom objects
 * @param <T> object to parse string into
 */
public interface Parser<T> {

    /**
     * Returning null will cause the default parser for this class type to be used.
     * This method is not called when parsing the option from the config file
     * @param source is only set when config option is being parsed from a command
     * @param inputValue the string value being parsed
     * @param currentValue the current parsed value of that config option
     * @since 1.0.4
     */
    T parse(@Nullable CommandSourceStack source, String inputValue, ParsedValue<T> currentValue);

    /**
     *
     * @param currentValue the current value is the value after {@link Parser#parse} or after loading the config file
     * @param valueToSet this is the value that is about to be set
     * @param config this is true when the value was loaded from the config file
     * @return The new value to be used
     * @since 1.0.4
     */
    default T modify(ParsedValue<T> currentValue, T valueToSet, boolean config) {
        return valueToSet;
    }

}

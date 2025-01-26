package ca.fxco.pistonlib.api.config;

import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

/**
 * Makes sure that the config value is valid, and converts the string inputs to a valid type.
 * Used to parse custom objects.
 *
 * @param <T> object to parse string into
 * @author FX
 * @since 1.0.4
 */
public interface Parser<T> {

    /**
     * This method is called when parsing the option from a string source, other than the config file.
     * Returning {@code null} causes the default parser for this class type to be used.
     *
     * @param currentValue the current parsed value of that config option
     * @param source       is only set when the config option is being parsed from a command
     * @param lastValue    the last value, doesn't have to be the same as the value in currentValue
     * @param inputValue   the string value being parsed
     * @return The parsed value to use, or {@code null} to use the default parser for this class type.
     * @since 1.0.4
     */
    T parse(ParsedValue<T> currentValue, @Nullable CommandSourceStack source,
            @Nullable T lastValue, String inputValue);

    /**
     * Called when setting the value of an option.
     * Allows you to modify the value.
     *
     * @param currentValue the current value is the value after {@link Parser#parse} or after loading the config file
     * @param valueToSet this is the value that's about to be set
     * @param config this is true when the value was loaded from the config file
     * @return The new value to be used
     * @since 1.0.4
     */
    default T modify(ParsedValue<T> currentValue, T valueToSet, boolean config) {
        return valueToSet;
    }

}

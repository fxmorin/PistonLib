package ca.fxco.api.pistonlib.config;

import org.jetbrains.annotations.Nullable;

/**
 * Use this interface to easily add ways to load/save custom types for the toml config file
 * This can be used to change what class toml uses to save your type
 * @author FX
 * @since 1.0.4
 */
public interface TypeConverter {

    /**
     * Attempts to save a value
     * @param value object to save
     * @param parsedValue value will be saved to this parsedValue
     * @return null if it's unable to save this value
     * @since 1.0.4
     */
    <T> @Nullable Object saveValue(T value, ParsedValue<T> parsedValue);

    /**
     * Attempts to load an Object
     * @param value TODO
     * @param parsedValue to load from
     * @return null if it's unable to load this value
     * @since 1.0.4
     */
    <T> @Nullable T loadValue(Object value, ParsedValue<T> parsedValue);
}

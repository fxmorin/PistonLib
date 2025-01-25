package ca.fxco.api.pistonlib.config;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Config Options should never be changed async.
 * They should be changed near the end of the tick,
 * highly recommended that you do this during the MinecraftServer tickables using
 * {@code minecraftServer.addTickable()} or during the network tick.
 * Such as during a packet.
 *
 * @author FX
 * @since 1.0.4
 */
public interface ConfigManager {

    /**
     * Initial Initialization, maps and parses the config fields.
     * Add {@code pistonlib-configmanager} entrypoint to your ConfigManager instance
     * which would call this method after all Config Options from other mods are collected
     *
     * @param modId mod id of the entrypoint provider
     * @param fieldProvider config fields added by other mods to your configManager
     * @since 1.0.4
     */
    void init(String modId, Map<String, List<Field>> fieldProvider);

    /**
     * Initializes the config.
     * Loading the values from the config and saving them.
     *
     * @since 1.0.4
     */
    void initializeConfig();

    /**
     * Resets all values to their default value. Called when leaving a server.
     * It's not recommended to call this on the server, since all the config changes are done in individual packets.
     *
     * @since 1.0.4
     */
    void resetAllToDefault();

    /**
     * Add a TypeConverter to ConfigManager
     *
     * @see TypeConverter
     * @param converter the TypeConverter to add
     * @since 1.0.4
     */
    void addConverter(TypeConverter converter);

    /**
     * Try to load value using type converters
     *
     * @param value default value
     * @param parsedValue parsedValue to load from
     * @return new value or {@code null} if config manager doesn't have converters
     * @since 1.0.4
     */
    <T> T tryLoadingValue(Object value, ParsedValue<T> parsedValue);

    /**
     * Try to load value using type converters
     *
     * @param value value to save
     * @param parsedValue parsed value to which the value is saved
     * @return new value or {@code null} if config manager doesn't have converters
     * @since 1.0.4
     */
    <T> Object trySavingValue(T value, ParsedValue<T> parsedValue);

    /**
     * Generates all values from a config class,
     * and then loads the config file and sets all there values
     *
     * @param fields fields to turn into ConfigValue and then add to parsedValues
     * @since 1.0.4
     */
    void loadConfigFields(Field[] fields);

    /**
     * Resets the parsed value to its default state,
     * then writes all the values to the config file
     *
     * @since 1.0.4
     */
    void resetAndSaveValue(ParsedValue<?> value);

    /**
     * Sets and saves a value, which was set using the config command.
     *
     * @since 1.0.4
     */
    void saveValueFromCommand(ParsedValue<?> value, CommandSourceStack source, String inputValue);

    /**
     * Gets a config value from its name.
     *
     * @return The parsed value associated with this name, or {@code null} if no value was found.
     * @since 1.0.4
     */
    ParsedValue<?> getParsedValue(String valueName);

    /**
     * Gets all the config values.
     *
     * @return A collection of all the parsed values.
     * @since 1.0.4
     */
    Collection<ParsedValue<?>> getParsedValues();
}

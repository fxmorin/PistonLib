package ca.fxco.api.pistonlib.config;

import ca.fxco.pistonlib.helpers.Utils;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import org.apache.commons.lang3.SerializationException;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class ConfigManager implements ConfigManagerEntrypoint {

    private final Path configPath;
    private final TomlWriter tomlWriter;
    private final List<TypeConverter> typeConverters = new ArrayList<>();

    private final Map<String, ParsedValue<?>> parsedValues = new LinkedHashMap<>();

    // TODO: Add a way to change config values in-game (with listeners to update the config file)

    /**
     * @param modId mod id of your mod
     * @param configClass the config class with all the ConfigValues
     * @since 1.0.4
     */
    public ConfigManager(String modId, Class<?> configClass) {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(modId + ".toml");
        this.tomlWriter = new TomlWriter();

        loadConfigFields(configClass.getDeclaredFields());
    }

    /**
     * Add {@code pistonlib-configmanager} entrypoint to your ConfigManager instance
     * which would call this method after all Config Options from other mods are collected
     *
     * @param modId mod id of the entrypoint provider
     * @param fieldProvider config fields added by other mods to your configManager
     * @since 1.0.4
     */
    public void init(String modId, Map<String, List<Field>> fieldProvider) {
        List<Field> fields = fieldProvider.get(modId);
        if (fields != null) {
            loadConfigFields(fields.toArray(new Field[0]));
        }

        Map<String, Object> loadedValues = loadValuesFromConf();
        if (loadedValues != null) {
            for (Map.Entry<String, Object> entry : loadedValues.entrySet()) {
                if (parsedValues.containsKey(entry.getKey())) {
                    parsedValues.get(entry.getKey()).setValueFromConfig(entry.getValue());
                }
            }
        }

        writeValuesToConf();
    }

    /**
     * Add a TypeConverter to ConfigManager
     *
     * @see TypeConverter
     * @param converter the TypeConverter to add
     * @since 1.0.4
     */
    public void addConverter(TypeConverter converter) {
        this.typeConverters.add(converter);
    }

    /**
     * Try to load value using type converters
     *
     * @param value default value
     * @param parsedValue parsedValue to load from
     * @return new value or {@code null} if config manager doesn't have converters
     * @since 1.0.4
     */
    public <T> T tryLoadingValue(Object value, ParsedValue<T> parsedValue) {
        for (TypeConverter converter : this.typeConverters) {
            T newValue = converter.loadValue(value, parsedValue);
            if (newValue != null) {
                return newValue;
            }
        }
        return null;
    }

    /**
     * Try to load value using type converters
     *
     * @param value value to save
     * @param parsedValue parsed value to which the value is saved
     * @return returns new value or {@code null} if config manager doesn't have converters
     * @since 1.0.4
     */
    public <T> Object trySavingValue(T value, ParsedValue<T> parsedValue) {
        for (TypeConverter converter : this.typeConverters) {
            Object newValue = converter.saveValue(value, parsedValue);
            if (newValue != null) {
                return newValue;
            }
        }
        return value;
    }

    /**
     * Generates all values from a config class,
     * and then loads the config file and sets all there values
     *
     * @param fields fields to turn into ConfigValue and then add to parsedValues
     * @since 1.0.4
     */
    public void loadConfigFields(Field[] fields) {
        nextField: for (Field field : fields) {

            // Only accept fields that are static & not final
            if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) continue;

            // Check for ConfigValue annotation
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof ConfigValue configValue) {
                    if (Arrays.stream(configValue.envType()).noneMatch(envType ->
                            envType == FabricLoader.getInstance().getEnvironmentType())) {
                        continue nextField; // Skip this field entirely
                    }
                    for (Class<? extends Condition> conditionClazz : configValue.condition()) {
                        Condition condition = Utils.createInstance(conditionClazz);
                        if (!condition.shouldInclude()) {
                            continue nextField; // Skip this field entirely
                        }
                    }
                    Parser<?>[] parsers = Utils.createInstances(Parser.class, configValue.parser());
                    Observer<?>[] observers = Utils.createInstances(Observer.class, configValue.observer());
                    ParsedValue<?> parsedValue = new ParsedValue<>(
                            field,
                            configValue.desc(),
                            configValue.more(),
                            configValue.keyword(),
                            configValue.category(),
                            configValue.requires(),
                            configValue.conflict(),
                            configValue.requiresRestart(),
                            configValue.fixes(),
                            parsers,
                            observers,
                            configValue.suggestions(),
                            this
                    );
                    parsedValues.put(parsedValue.getName(), parsedValue);
                    break;
                }
            }
        }
    }

    /**
     * Loads values from config file
     *
     * @return values from config file or {@code null} if it doesn't exist
     * @since 1.0.4
     */
    @SuppressWarnings("unchecked")
    private @Nullable Map<String, Object> loadValuesFromConf() {
        if (Files.exists(configPath)) {
            try {
                Map<String, Object> values = new HashMap<>();
                new Toml().read(configPath.toFile()).toMap().values().forEach(value -> {
                    if (value instanceof Map<?, ?>) {
                        values.putAll((Map<String, Object>) value);
                    }
                });
                return values;
            } catch (IllegalStateException e) {
                throw new SerializationException(e);
            }
        }
        return null;
    }

    /**
     * Writes all the values to the config file
     *
     * @since 1.0.4
     */
    private void writeValuesToConf() {
        try {
            Files.createDirectories(configPath.getParent());
            Map<String, Map<String, Object>> savedValues = new LinkedHashMap<>();
            savedValues.put("NONE", new LinkedHashMap<>());
            for (Map.Entry<String, ParsedValue<?>> entry : parsedValues.entrySet()) {
                ParsedValue<?> parsedValue = entry.getValue();
                Category category = parsedValue.getCategories().stream().findAny().orElse(null);
                if (category != null) {
                    savedValues.computeIfAbsent(category.name(), string ->
                            new LinkedHashMap<>()).put(entry.getKey(), parsedValue.getValueForConfig());
                    continue;
                }
                savedValues.get("NONE").put(entry.getKey(), parsedValue.getValueForConfig());
            }
            tomlWriter.write(savedValues, configPath.toFile());
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Resets the parsed value to its default state,
     * then writes all the values to the config file
     *
     * @since 1.0.4
     */
    public void resetAndSaveValue(ParsedValue<?> value) {
        value.reset();
        writeValuesToConf();
    }

    /**
     * Sets and saves a value, which was set using the config command.
     *
     * @since 1.0.4
     */
    public void saveValueFromCommand(ParsedValue<?> value, CommandSourceStack source, String inputValue) {
        value.parseValue(source, inputValue);
        writeValuesToConf();
    }

    /**
     * Gets a config value from its name.
     *
     * @return The parsed value associated with this name, or {@code null} if no value was found.
     * @since 1.0.4
     */
    public ParsedValue<?> getParsedValue(String valueName) {
        return parsedValues.get(valueName);
    }

    /**
     * Gets all the config values.
     *
     * @return A collection of all the parsed values.
     * @since 1.0.4
     */
    public Collection<ParsedValue<?>> getParsedValues() {
        return parsedValues.values();
    }

    @Override
    public ConfigManager getConfigManager() {
        return this;
    }

}

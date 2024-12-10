package ca.fxco.pistonlib.config;

import ca.fxco.api.pistonlib.config.*;
import ca.fxco.api.pistonlib.config.Observer;
import ca.fxco.pistonlib.PistonLibConfig;
import ca.fxco.pistonlib.helpers.Utils;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.loader.api.FabricLoader;
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
 * Config Options should never be changed async. They should be changed near the end of the tick, highly recommended
 * that you do this during the MinecraftServer tickables using minecraftServer.addTickable() or during the network
 * tick. Such as during a packet
 */
public class ConfigManager {

    private final Path configPath;
    private final TomlWriter tomlWriter;
    private final List<TypeConverter> typeConverters = new ArrayList<>();

    private final Map<String, ParsedValue<?>> parsedValues = new LinkedHashMap<>();

    // TODO: Add a way to change config values in-game (with listeners to update the config file)

    public ConfigManager(String modId, Class<?> configClass) {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(modId + ".toml");
        this.tomlWriter = new TomlWriter();

        loadConfigClass(configClass);

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

    public void addConverter(TypeConverter converter) {
        this.typeConverters.add(converter);
    }

    public <T> T tryLoadingValue(Object value, ParsedValue<T> parsedValue) {
        for (TypeConverter converter : this.typeConverters) {
            T newValue = converter.loadValue(value, parsedValue);
            if (newValue != null) {
                return newValue;
            }
        }
        return null;
    }

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
     * Generates all values from a config class and then loads the config file and sets all there values
     */
    public void loadConfigClass(Class<?> configClass) {
        nextField: for (Field field : configClass.getDeclaredFields()) {

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
                            this
                    );
                    parsedValues.put(parsedValue.getName(), parsedValue);
                    break;
                }
            }
        }
    }

    private @Nullable Map<String, Object> loadValuesFromConf() {
        if (Files.exists(configPath)) {
            try {
                return new Toml().read(configPath.toFile()).toMap();
            } catch (IllegalStateException e) {
                throw new SerializationException(e);
            }
        }
        return null;
    }

    private void writeValuesToConf() {
        try {
            Files.createDirectories(configPath.getParent());
            Map<String, Map<String, Object>> savedValues = new LinkedHashMap<>();
            savedValues.put("NONE", new LinkedHashMap<>());
            for (Map.Entry<String, ParsedValue<?>> entry : parsedValues.entrySet()) {
                ParsedValue<?> parsedValue = entry.getValue();
                Category category = parsedValue.getCategories().stream().findAny().orElse(null);
                if (category != null) {
                    savedValues.putIfAbsent(category.name(), new LinkedHashMap<>());
                    savedValues.get(category.name()).put(entry.getKey(), parsedValue.getValueForConfig());
                    continue;
                }
                savedValues.get("NONE").put(entry.getKey(), parsedValue.getValueForConfig());
            }
            tomlWriter.write(savedValues, configPath.toFile());
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

}

package ca.fxco.pistonlib.config;

import ca.fxco.api.pistonlib.config.*;
import ca.fxco.api.pistonlib.config.ConfigManager;
import ca.fxco.api.pistonlib.config.Observer;
import ca.fxco.api.pistonlib.util.BufferUtils;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.helpers.Utils;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import io.netty.buffer.ByteBuf;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.storage.LevelResource;
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
 * The base implementation of the config manager.
 *
 * @author FX
 * @since 1.0.4
 */
public class ConfigManagerImpl implements ConfigManager, ConfigManagerEntrypoint {

    private static final boolean DEBUG_CONFIG = false;
    private static final boolean DEBUG_NETWORK = false;

    private final String modId;
    private final TomlWriter tomlWriter;
    private final List<TypeConverter> typeConverters = new ArrayList<>();

    private final Map<String, ParsedValue<?>> parsedValues = new LinkedHashMap<>();

    /**
     * @param modId mod id of your mod
     * @param configClass the config class with all the ConfigValues
     * @since 1.0.4
     */
    public ConfigManagerImpl(String modId, Class<?> configClass) {
        this.modId = modId;
        this.tomlWriter = new TomlWriter();

        loadConfigFields(configClass.getDeclaredFields());
    }

    @Override
    public void init(String modId, Map<String, List<Field>> fieldProvider) {
        List<Field> fields = fieldProvider.get(modId);
        if (fields != null) {
            loadConfigFields(fields.toArray(new Field[0]));
        }

        initializeConfig();
    }

    @Override
    public void initializeConfig() {
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

    @Override
    public void resetAllToDefault() {
        for (ParsedValue<?> value : parsedValues.values()) {
            value.reset();
        }
    }

    @Override
    public void addConverter(TypeConverter converter) {
        this.typeConverters.add(converter);
    }

    @Override
    public <T> T tryLoadingValue(Object value, ParsedValue<T> parsedValue) {
        for (TypeConverter converter : this.typeConverters) {
            T newValue = converter.loadValue(value, parsedValue);
            if (newValue != null) {
                return newValue;
            }
        }
        return null;
    }

    @Override
    public <T> Object trySavingValue(T value, ParsedValue<T> parsedValue) {
        for (TypeConverter converter : this.typeConverters) {
            Object newValue = converter.saveValue(value, parsedValue);
            if (newValue != null) {
                return newValue;
            }
        }
        return value;
    }

    @Override
    public void loadConfigFields(Field[] fields) {
        nextField: for (Field field : fields) {

            // Only accept fields that are static & not final
            if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

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
                    ca.fxco.api.pistonlib.config.Observer<?>[] observers = Utils.createInstances(Observer.class, configValue.observer());
                    ParsedValue<?> parsedValue = new ParsedValueImpl<>(
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

    @SuppressWarnings("rawtypes")
    @Override
    public Map<ParsedValue, Object> readValuesFromBuffer(FriendlyByteBuf buffer) {
        // List of values to change
        String[] values = BufferUtils.readFromBuffer(buffer, String[].class);
        // Size of each value, in-case the client doesn't have that value (outdated client)
        // TODO: Could remove this by including the mod version within a register packet
        short[] sizes = new short[values.length];
        for (int i = 0; i < values.length; i++) {
            sizes[i] = buffer.readShort();
        }

        Map<ParsedValue, Object> changesMap = new HashMap<>(values.length);
        for (int i = 0; i < values.length; i++) {
            ParsedValue<?> parsedValue = getParsedValue(values[i]);
            if (parsedValue != null) {
                changesMap.put(parsedValue, parsedValue.readValueFromBuffer(buffer));
            } else {
                buffer.skipBytes(sizes[i]);
            }
        }

        if (DEBUG_NETWORK) {
            System.out.println("[PistonLib] Reading values from buffer:");
            for (var entry : changesMap.entrySet()) {
                System.out.println(" - " + entry.getKey().getName() + ": " + entry.getValue());
            }
        }

        return changesMap;
    }

    @Override
    public void writeValuesToBuffer(FriendlyByteBuf buffer, ParsedValue<?>[] values) {
        // Save list of values
        String[] valueIds = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            valueIds[i] = values[i].getName();
        }
        BufferUtils.writeToBuffer(buffer, valueIds);

        // Skip the size table for now, we write this at the end
        short[] sizes = new short[valueIds.length];
        int sizeTableIndex = buffer.writerIndex();
        int sizeTableSize = sizes.length * 2;
        buffer.writerIndex(sizeTableIndex + sizeTableSize);

        // Save each value individually
        int lastIndex = buffer.writerIndex();
        for (int i = 0; i < valueIds.length; i++) {
            values[i].writeValueToBuffer(buffer);
            int currentIndex = buffer.writerIndex();
            sizes[i] = (short) (currentIndex - lastIndex);
            lastIndex = currentIndex;
        }

        // Write sizes table
        ByteBuf reservedBuffer = buffer.retainedSlice(sizeTableIndex, sizeTableSize);
        reservedBuffer.writerIndex(0);
        for (int i = 0; i < valueIds.length; i++) {
            reservedBuffer.writeShort(sizes[i]);
        }
        reservedBuffer.release();

        if (DEBUG_NETWORK) {
            System.out.println("[PistonLib] Writing values to buffer:");
            for (int i = 0; i < valueIds.length; i++) {
                String id = valueIds[i];
                Object value = values[i].getValue();
                System.out.println(" - " + id + ": " + value);
            }
        }
    }

    @Override
    public void resetAndSaveValue(ParsedValue<?> value) {
        value.reset();
        writeValuesToConf();
    }

    @Override
    public void saveValueFromCommand(ParsedValue<?> value, CommandSourceStack source, String inputValue) {
        value.parseValue(source, inputValue);
        writeValuesToConf();
    }

    @Override
    public ParsedValue<?> getParsedValue(String valueName) {
        return parsedValues.get(valueName);
    }

    @Override
    public Collection<ParsedValue<?>> getParsedValues() {
        return parsedValues.values();
    }

    @Override
    public ca.fxco.api.pistonlib.config.ConfigManager getConfigManager() {
        return this;
    }

    /**
     * @return Empty optional if we aren't able to save values at the moment
     */
    private Optional<Path> getConfigFile() {
        return PistonLib.getServer()
                .map(s -> s.getWorldPath(LevelResource.ROOT).resolve(modId + ".toml"));
    }

    /**
     * Loads values from config file
     *
     * @return values from config file or {@code null} if it doesn't exist
     * @since 1.0.4
     */
    @SuppressWarnings("unchecked")
    private @Nullable Map<String, Object> loadValuesFromConf() {
        Path configPath = getConfigFile().orElse(null);
        if (configPath == null) {
            return null;
        }
        if (Files.exists(configPath)) {
            try {
                Map<String, Object> values = new HashMap<>();
                new Toml().read(configPath.toFile()).toMap().values().forEach(value -> {
                    if (value instanceof Map<?, ?>) {
                        values.putAll((Map<String, Object>) value);
                    }
                });
                if (DEBUG_CONFIG) {
                    System.out.println("[PistonLib] Loading values from config:");
                    for (var entry : values.entrySet()) {
                        System.out.println(" - " + entry.getKey() + ": " + entry.getValue());
                    }
                }
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
        getConfigFile().ifPresent(configPath -> {
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
                if (DEBUG_CONFIG) {
                    System.out.println("[PistonLib] Saving values to config:");
                    for (Map<String, Object> map : savedValues.values()) {
                        for (var entry : map.entrySet()) {
                            System.out.println(" - " + entry.getKey() + ": " + entry.getValue());
                        }
                    }
                }
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        });
    }
}

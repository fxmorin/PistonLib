package ca.fxco.pistonlib.config;

import ca.fxco.api.pistonlib.config.*;
import ca.fxco.api.pistonlib.config.ParsedValue;
import ca.fxco.api.pistonlib.util.BufferUtils;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.network.PLServerNetwork;
import ca.fxco.pistonlib.network.packets.ClientboundModifyConfigPacket;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.primitives.ImmutableIntArray;
import com.google.common.primitives.Primitives;
import lombok.Getter;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ParsedValueImpl<T> implements ParsedValue<T> {

    private final Field field;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final String[] moreInfo;
    @Getter
    private final Set<String> keywords;
    @Getter
    private final Set<Category> categories;
    @Getter
    private final String[] requires;
    @Getter
    private final String[] conflicts;
    private final ImmutableIntArray fixes;
    private final boolean requiresRestart;
    private final T defaultValue; // Set by the recommended option
    private final Parser<T>[] parsers;
    private final Observer<T>[] observers;
    @Getter
    private final String[] suggestions;
    private final ConfigManager configManager;
    @Getter
    private T valueToSave;

    public ParsedValueImpl(Field field, String desc, String[] more, String[] keywords, Category[] categories,
                           String[] requires, String[] conflicts, boolean requiresRestart, int[] fixes,
                           Parser<?>[] parsers, Observer<?>[] observers, String[] suggestions,
                           ConfigManager configManager) {
        this.field = field;
        this.name = field.getName();
        this.description = desc;
        this.moreInfo = more;
        this.keywords = ImmutableSet.copyOf(keywords);
        this.categories = ImmutableSet.copyOf(categories);
        this.requires = requires;
        this.conflicts = conflicts;
        this.requiresRestart = requiresRestart;
        this.fixes = ImmutableIntArray.copyOf(fixes);
        this.parsers = (Parser<T>[]) parsers;
        this.observers = (Observer<T>[]) observers;
        this.suggestions = suggestions;
        this.defaultValue = getValue();
        this.configManager = configManager;
        this.valueToSave = getValue();
    }

    @Override
    public int[] getFixes() {
        return fixes.toArray();
    }

    @Override
    public boolean requiresRestart() {
        return requiresRestart;
    }

    @Override
    public void reset() {
        setValue(this.defaultValue);
    }

    @Override
    public boolean isDefaultValue() {
        return this.defaultValue.equals(getValue());
    }

    @Override
    public void setValue(T value) {
        setValue(value, false);
    }


    @Override
    public void setValue(T value, boolean load) {
        boolean updateClients = false;
        try {
            T currentValue = getValue();
            if (!value.equals(currentValue)) {
                if (!load) {
                    for (Parser<T> parser : this.parsers) {
                        value = parser.modify(this, value, false);
                    }
                }
                this.valueToSave = value;
                if (!requiresRestart) {
                    this.field.set(null, value);
                    for (Observer<T> observer : this.observers) {
                        if (load) {
                            observer.onLoad(this, isDefaultValue());
                        } else {
                            observer.onChange(this, currentValue, value);
                        }
                    }
                    updateClients = true;
                }
            } else if (load) {
                for (Observer<T> observer : this.observers) {
                    observer.onLoad(this, isDefaultValue());
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        if (updateClients && !load) {
            // Send the config value change to clients
            PistonLib.getServer().ifPresent(server ->
                    PLServerNetwork.sendToAllExternalClients(
                            server,
                            ClientboundModifyConfigPacket.fromCollection(List.of(this))
                    ));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T readValueFromBuffer(FriendlyByteBuf buffer) {
        // TODO: Convert to Codec's in 1.20.5+
        return (T) BufferUtils.readFromBuffer(buffer, this.defaultValue.getClass());
    }

    @Override
    public void writeValueToBuffer(FriendlyByteBuf buffer) {
        // TODO: Convert to Codec's in 1.20.5+
        BufferUtils.writeToBuffer(buffer, this.getValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getValue() {
        try {
            return (T) this.field.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void setValueFromConfig(Object value) {
        T newValue = loadValueFromConfig(value);
        if (newValue == null) {
            newValue = this.configManager.tryLoadingValue(value, this);
        }
        if (newValue != null) {
            for (Parser<T> parser : this.parsers) {
                newValue = parser.modify(this, newValue, true);
            }
            setValue(newValue, true);
        }
    }

    @Override
    public Object getValueForConfig() {
        return this.configManager.trySavingValue(this.valueToSave, this);
    }

    @Override
    public void parseValue(CommandSourceStack source, String inputValue) {
        boolean useDefault = true;
        for (Parser<T> parser : this.parsers) {
            T newValue = parser.parse(source, inputValue, this);
            if (newValue != null) {
                setValue(newValue);
                useDefault = false;
            }
        }
        if (useDefault) {
            T newValue = parseValueFromString(inputValue);
            if (newValue != null) {
                setValue(newValue);
            }
        }
    }

    @Override
    public boolean matchesTerm(String search) {
        search = search.toLowerCase(Locale.ROOT);
        if (this.name.toLowerCase(Locale.ROOT).contains(search)) {
            return true;
        }
        return Sets.newHashSet(this.description.toLowerCase(Locale.ROOT).split("\\W+")).contains(search);
    }

    @Override
    public boolean doKeywordMatchSearch(String search) {
        search = search.toLowerCase(Locale.ROOT);
        for (String keyword : this.keywords) {
            if (keyword.toLowerCase(Locale.ROOT).startsWith(search)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doCategoryMatchSearch(String search) {
        search = search.toLowerCase(Locale.ROOT);
        for (Category category : this.categories) {
            if (category.name().toLowerCase(Locale.ROOT).equals(search)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private @Nullable T loadValueFromConfig(Object value) {
        Class<?> clazz = this.defaultValue.getClass();
        if (clazz == value.getClass()) {
            return (T) clazz.cast(value);
        } else if (clazz.isEnum() && value instanceof String str) {
            Object e = Enum.valueOf((Class<? extends Enum>)clazz, str);
            return (T) clazz.cast(e);
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private T parseValueFromString(String inputValue) {
        Class<T> clazz = (Class<T>) this.defaultValue.getClass();
        if (clazz.isPrimitive()) {
            return (T) parsePrimitiveValue(clazz, inputValue);
        }
        if (Primitives.isWrapperType(clazz)) {
            return (T) parsePrimitiveValue(Primitives.unwrap(clazz), inputValue);
        }
        if (clazz.isEnum()) {
            return clazz.cast(Enum.valueOf((Class<? extends Enum>)clazz, inputValue));
        }
        return null;
    }

    private Object parsePrimitiveValue(Class<?> clazz, String inputValue) {
        if (clazz == boolean.class) {
            return Boolean.parseBoolean(inputValue);
        } else if (clazz == int.class) {
            return Integer.parseInt(inputValue);
        } else if (clazz == long.class) {
            return Long.parseLong(inputValue);
        } else if (clazz == float.class) {
            return Float.parseFloat(inputValue);
        } else if (clazz == double.class) {
            return Double.parseDouble(inputValue);
        } else if (clazz == byte.class) {
            return Byte.parseByte(inputValue);
        } else if (clazz == short.class) {
            return Short.parseShort(inputValue);
        } else if (clazz == char.class) {
            return inputValue.charAt(0);
        }
        return inputValue;
    }
}

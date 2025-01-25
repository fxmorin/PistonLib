package ca.fxco.api.pistonlib.config;

import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Holds necessary info about a config field,
 * as well as providing methods to set/get its value
 *
 * @param <T> the class of value
 * @author FX
 * @since 1.0.4
 */
public interface ParsedValue<T> {

    /**
     * Gets the name of the value.
     * This is also used as the ID.
     *
     * @return The name
     * @since 1.0.4
     */
    String getName();

    /**
     * Gets the description of the value
     *
     * @return The description
     * @since 1.0.4
     */
    String getDescription();

    /**
     * Gets extra information lines about the value
     *
     * @return A string array containing more info
     * @since 1.0.4
     */
    String[] getMoreInfo();

    /**
     * Gets the keywords of the value,
     * used to improve searching.
     *
     * @return A set containing all the keywords
     * @since 1.0.4
     */
    Set<String> getKeywords();

    /**
     * Gets the categories associated with this value
     *
     * @return A set containing all the categories
     * @since 1.0.4
     */
    Set<Category> getCategories();

    /**
     * Gets ids of required values.
     * These values must be enabled for this value to work.
     *
     * @return An array of id's
     * @since 1.0.4
     */
    String[] getRequires();

    /**
     * Gets ids of conflicting values.
     * These values must be turned off for this value to work.
     *
     * @return An array of id's
     * @since 1.0.4
     */
    String[] getConflicts();

    /**
     * Gets the mojira fix id's associated with this value.
     * You can check the issue by going to: <a href="https://bugs.mojang.com/browse/MC-27056">https://bugs.mojang.com/browse/MC-&lt;id&gt</a>
     *
     * @return An array of mojira fix id's
     * @since 1.0.4
     */
    int[] getFixes();

    /**
     * Checks if this value requires a restart to work.
     *
     * @return {@code true} if it needs a restart, otherwise {@code false}
     * @since 1.0.4
     */
    boolean requiresRestart();

    /**
     * Gets command suggestions for auto-completion.
     *
     * @return An array of suggestions
     * @since 1.0.4
     */
    String[] getSuggestions();

    /**
     * Gets the value that should be saved.
     * This differs from the actual value when the value requires a restart.
     *
     * @return The value to save
     * @since 1.0.4
     */
    T getValueToSave();

    /**
     * Sets this value to its default value
     *
     * @since 1.0.4
     */
    void reset();

    /**
     * Check if it's currently the default value
     *
     * @return {@code true} if it's currently the default value, otherwise {@code false}
     * @since 1.0.4
     */
    boolean isDefaultValue();

    /**
     * Used to set the value of parsed value
     *
     * @param value object to set field's value to
     * @since 1.0.4
     */
    void setValue(T value);


    /**
     * Used to set the value of parsed value
     *
     * @param value object to set field's value to
     * @param load is method called on load or after
     * @since 1.0.4
     */
    void setValue(T value, boolean load);

    /**
     * Used to get the value of parsed value
     *
     * @return value of the field assigned to this parsed value
     * @since 1.0.4
     */
    T getValue();

    /**
     * Gets all the values that should be tested within the gametests.
     *
     * @return An array of values to test
     * @since 1.0.4
     */
    T[] getAllTestingValues();

    /**
     * Shouldn't be used unless loading from the config
     *
     * @param value value to set
     * @since 1.0.4
     */
    void setValueFromConfig(Object value);

    /**
     * Gets the value that should be used within the config file
     *
     * @return The value to use within the config file
     * @since 1.0.4
     */
    Object getValueForConfig();

    /**
     * Used when attempting to parse the value from a command as a string
     *
     * @param source command source stack used by command
     * @param inputValue string from command to parse
     * @since 1.0.4
     */
    void parseValue(@Nullable CommandSourceStack source, String inputValue);

    /**
     * Checks if the config value name or its description matches the search term
     *
     * @param search the string what is being searched
     * @return {@code true} if the value name or its description matches the search term, otherwise {@code false}
     * @since 1.0.4
     */
    boolean matchesTerm(String search);

    /**
     * Checks if the search term matches one of the config values keywords
     *
     * @param search the string what is being searched
     * @return {@code true} if the search term matches one of the values keywords, otherwise {@code false}
     * @since 1.0.4
     */
    boolean doKeywordMatchSearch(String search);

    /**
     * Checks if the config value contains a category which matches the search term
     *
     * @param search the string what is being searched
     * @return {@code true} if the value contains a category which matches the search term, otherwise {@code false}
     * @since 1.0.4
     */
    boolean doCategoryMatchSearch(String search);
}

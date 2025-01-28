package ca.fxco.pistonlib.api.config;

/**
 * The Condition class is used to determine if a config option should be loaded by the config manager
 *
 * @author FX
 * @since 1.0.4
 */
public interface Condition {

    /**
     * Returns if the config option should be included.
     * By returning false, the config option won't be loaded by
     * the config manager and remains its default value.
     *
     * @return {@code true} if this condition should be included. Otherwise {@code false}
     *
     * @since 1.0.4
     */
    boolean shouldInclude();
}

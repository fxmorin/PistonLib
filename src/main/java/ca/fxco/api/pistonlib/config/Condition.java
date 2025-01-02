package ca.fxco.api.pistonlib.config;

/**
 * The Condition class is used to determine if a config option should be loaded by the config manager
 * @author FX
 * @since 1.0.4
 */
public interface Condition {

    /**
     * Returns if the config option should be included. By returning false, the config option will not be loaded by
     * the config manager and will remain as its default value.
     * @since 1.0.4
     */
    boolean shouldInclude();
}

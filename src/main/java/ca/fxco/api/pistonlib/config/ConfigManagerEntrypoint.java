package ca.fxco.api.pistonlib.config;

/**
 * Used to get config manager from an entrypoint.
 * Allows you to keep the config manager in a private field.
 *
 * @author Foxy
 * @since 1.0.4
 */
public interface ConfigManagerEntrypoint {

    /**
     * Proxy method for entrypoint
     *
     * @return this config manager
     * @since 1.0.4
     */
    ConfigManager getConfigManager();

}

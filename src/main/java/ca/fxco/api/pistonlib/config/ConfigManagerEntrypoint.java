package ca.fxco.api.pistonlib.config;

/**
 * Used to get config manager if the field is private
 * @author Foxy
 * @since 1.0.4
 */
public interface ConfigManagerEntrypoint {

    /**
     * Proxy method for entrypoint
     * @since 1.0.4
     */
    ConfigManager getConfigManager();

}

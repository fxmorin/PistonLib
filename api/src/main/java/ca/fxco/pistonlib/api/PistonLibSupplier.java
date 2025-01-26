package ca.fxco.pistonlib.api;

import ca.fxco.pistonlib.api.config.ConfigManager;

/**
 * This interface gives mod developers access to internal PistonLib implementations,
 * without needing to rely on them directly.
 *
 * @author FX
 * @since 1.0.4
 */
public interface PistonLibSupplier {

    /**
     * Creates a simple config manager from PistonLib.
     * Use this if you don't want to create your own config manager from scratch!
     *
     * @param modId       Your mod's id
     * @param configClass The class containing all your config's
     * @return A new {@link ConfigManager}
     * @since 1.0.4
     */
    ConfigManager createSimpleConfigManager(String modId, Class<?> configClass);
}

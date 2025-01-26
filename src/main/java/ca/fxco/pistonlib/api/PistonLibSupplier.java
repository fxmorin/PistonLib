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

    ConfigManager createSimpleConfigManager(String modId, Class<?> configClass);
}

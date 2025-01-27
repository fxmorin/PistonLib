package ca.fxco.pistonlib.api;

import ca.fxco.pistonlib.api.config.ConfigManager;
import ca.fxco.pistonlib.api.pistonLogic.structure.StructureGroup;

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

    /**
     * Creates a Structure Group based on the environment.
     * The client structure group holds some rendering cache.
     *
     * @param clientSide if this is being called from the client
     * @return If on the client, a {@code ClientStructureGroup}, otherwise a {@code ServerStructureGroup}
     * @since 1.2.0
     */
    StructureGroup createStructureGroup(boolean clientSide);
}

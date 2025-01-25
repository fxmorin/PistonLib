package ca.fxco.pistonlib.api;

import ca.fxco.pistonlib.api.blockEntity.PLBlockEntities;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamilies;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyGroups;

/**
 * Interface for registering custom piston families and sticky groups.
 *
 * @author Space Walker
 * @since 1.0.4
 */
public interface PistonLibInitializer {

    /**
     * The first method to be called.
     * Passes a supplier to give developers access to internal implementations.
     *
     * @since 1.0.4
     */
    void initialize(PistonLibSupplier supplier);

    /**
     * Register custom piston families through {@link PistonFamilies#register}
     *
     * @since 1.0.4
     */
    void registerPistonFamilies();

    /**
     * Register custom sticky groups through {@link StickyGroups#register}
     *
     * @since 1.0.4
     */
    void registerStickyGroups();

    /**
     * Initialize custom registries, blocks, items, etc.
     * It is recommended to register custom moving block entity types through {@link PLBlockEntities#register}
     *
     * @since 1.0.4
     */
    void bootstrap();

}

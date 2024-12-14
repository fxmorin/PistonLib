package ca.fxco.api.pistonlib;

/**
 * Interface for registering custom piston families and sticky groups.
 * @author Space Walker
 * @since 1.0.4
 */
public interface PistonLibInitializer {

    /**
     * Register custom piston families through
     * {@linkplain ca.fxco.api.pistonlib.pistonLogic.families.PistonFamilies#register}
     * @since 1.0.4
     */
    void registerPistonFamilies();

    /**
     * Register custom sticky groups through
     * {@linkplain ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroups#register}
     * @since 1.0.4
     */
    void registerStickyGroups();

    /**
     * Initialize custom registries, blocks, items, etc.
     * 
     * It is recommended to register custom moving block entity types through
     * {@linkplain ca.fxco.api.pistonlib.blockEntity.PLBlockEntities#register}
     * @since 1.0.4
     */
    void bootstrap();

}

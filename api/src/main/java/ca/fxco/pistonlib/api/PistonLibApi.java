package ca.fxco.pistonlib.api;

/**
 * The main API class for PistonLib.
 * This is accessible from the API, and provides access to internal PistonLib classes.
 * <p>
 * This is populated by PistonLib. So you need to make sure that PistonLib has been initialized
 * before accessing this class.
 *
 * @author FX
 * @since 1.2.0
 */
public class PistonLibApi {

    private static PistonLibSupplier SUPPLIER;

    /**
     * Gets the {@link PistonLibSupplier}
     * This provides you with access to more internal PistonLib implementations.
     *
     * @return The PistonLibSupplier
     * @since 1.2.0
     */
    public static PistonLibSupplier getSupplier() {
        return SUPPLIER;
    }

    /**
     * For internal PistonLib use only.
     * Sets the {@link PistonLibSupplier} within the API
     *
     * @param supplier The supplier to set
     * @since 1.2.0
     */
    public static void setSupplier(PistonLibSupplier supplier) {
        if (PistonLibApi.SUPPLIER != null) {
            throw new RuntimeException("PistonLibSupplier must only be set by PistonLib!");
        }
        PistonLibApi.SUPPLIER = supplier;
    }
}

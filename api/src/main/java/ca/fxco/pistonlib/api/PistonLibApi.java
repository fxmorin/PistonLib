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

    protected static PistonLibSupplier SUPPLIER;

    public static PistonLibSupplier getSupplier() {
        return SUPPLIER;
    }

    public static void setSupplier(PistonLibSupplier supplier) {
        if (PistonLibApi.SUPPLIER != null) {
            throw new RuntimeException("PistonLibSupplier must only be set by PistonLib!");
        }
        PistonLibApi.SUPPLIER = supplier;
    }
}

package ca.fxco.pistonlib.api.toggle;

import java.util.function.BooleanSupplier;

/**
 * Interface to use integrate with minecraft's feature flag system.
 *
 * @author FX
 * @since 1.0.4
 */
public interface Toggleable {

    /**
     * Checks if this toggleable is currently disabled.
     *
     * @return is this object disabled
     */
    BooleanSupplier pl$getIsDisabled();

}

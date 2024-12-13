package ca.fxco.api.pistonlib.toggle;

import java.util.function.BooleanSupplier;

/**
 * @author FX
 * @param <T> object to toggle
 * @since 1.0.4
 */
public interface ToggleableProperties<T> {

    /**
     * Allows you to conditionally set if this object is disabled
     * @param isDisabled boolean supplier to set disabled to
     * @since 1.0.4
     */
    T pl$setDisabled(BooleanSupplier isDisabled);

}

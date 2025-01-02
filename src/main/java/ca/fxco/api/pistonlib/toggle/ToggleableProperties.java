package ca.fxco.api.pistonlib.toggle;

import java.util.function.BooleanSupplier;

/**
 * Toggleable for properties.
 *
 * @param <T> object to toggle
 * @author FX
 * @since 1.0.4
 */
public interface ToggleableProperties<T> {

    /**
     * Allows you to conditionally set if this object is disabled.
     *
     * @param isDisabled boolean supplier to set disabled to
     * @return self, used in builder constructs
     * @since 1.0.4
     */
    T pl$setDisabled(BooleanSupplier isDisabled);

}

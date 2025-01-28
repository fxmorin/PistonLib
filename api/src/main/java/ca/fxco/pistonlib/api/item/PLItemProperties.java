package ca.fxco.pistonlib.api.item;

import java.util.function.BooleanSupplier;

import ca.fxco.pistonlib.api.toggle.Toggleable;
import ca.fxco.pistonlib.api.toggle.ToggleableProperties;
import net.minecraft.world.item.Item.Properties;

/**
 * This interface collects all custom behavior that is injected into {@code Item.Properties}s.
 * It provides dummy implementations for all custom behavior. The actual implementations
 * of these methods are provided in their respective Mixin classes.
 *
 * @author Space Walker
 * @since 1.0.4
 */
public interface PLItemProperties extends Toggleable, ToggleableProperties<Properties> {


    // Toggleable

    @Override
    default BooleanSupplier pl$getIsDisabled() {
        return null;
    }


    // ToggleableProperties

    @Override
    default Properties pl$setDisabled(BooleanSupplier isDisabled) {
        return null;
    }
}

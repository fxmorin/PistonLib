package ca.fxco.pistonlib.api.block;

import java.util.function.BooleanSupplier;

import ca.fxco.pistonlib.api.toggle.Toggleable;
import ca.fxco.pistonlib.api.toggle.ToggleableProperties;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * This interface collects all custom behavior that is injected into {@code BlockBehaviour.Properties}'s.
 * It provides dummy implementations for all custom behavior. The actual implementations
 * of these methods are provided in their respective Mixin classes.
 *
 * @author Space Walker
 * @since 1.0.4
 */
public interface PLBlockProperties extends Toggleable, ToggleableProperties<Properties> {


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

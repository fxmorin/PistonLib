package ca.fxco.api.pistonlib.toggle;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlagUniverse;

import java.util.function.BooleanSupplier;

/**
 * @author FX
 * @since 1.0.4
 */
public interface Toggleable {

    FeatureFlagUniverse FAKE_UNIVERSE = new FeatureFlagUniverse("fake_universe");
    FeatureFlag NEVER_ENABLED = new FeatureFlag(FAKE_UNIVERSE, 64);
    FeatureFlagSet NEVER_ENABLED_SET = FeatureFlagSet.of(NEVER_ENABLED);

    /**
     * @return is this object disabled
     */
    BooleanSupplier pl$getIsDisabled();

}

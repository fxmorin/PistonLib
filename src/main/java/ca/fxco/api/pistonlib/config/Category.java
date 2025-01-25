package ca.fxco.api.pistonlib.config;

/**
 * Categories for config values.
 * In alphabetical order
 *
 * @see ConfigValue
 * @author FX
 * @since 1.0.4
 */
public enum Category {

    /**
     * An experimental config value, use with caution
     *
     * @since 1.0.4
     */
    EXPERIMENTAL,

    /**
     * Extreme means this has a Major impact on how pistons work and likely
     * breaks any contraption made without it being on
     *
     * @since 1.0.4
     */
    EXTREME,

    /**
     * A feature toggle, features aren't mechanics
     *
     * @since 1.0.4
     */
    FEATURE,

    /**
     * Fixes a vanilla bug
     *
     * @since 1.0.4
     */
    FIX,

    /**
     * A core mechanic, disabling this probably causes many things to stop working
     *
     * @since 1.0.4
     */
    MECHANIC,

    /**
     * Related to the merging api
     *
     * @since 1.0.4
     */
    MERGING,

    /**
     * An optimization config value, performance go brrrr
     *
     * @since 1.0.4
     */
    OPTIMIZATION,

    /**
     * A tweak, when it simply tweaks a setting
     *
     * @since 1.1.0
     */
    TWEAK,

    /**
     * Work In Progress, it's still missing some features
     *
     * @since 1.0.4
     */
    WIP

}

package ca.fxco.api.pistonlib.config;

/**
 * Categories for config values
 * @see ConfigValue
 * @author FX
 * @since 1.0.4
 */
public enum Category {

    /** Fixes a vanilla bug
     * @since 1.0.4
     */
    FIX,

    /** A feature toggle, features are not mechanics
     * @since 1.0.4
     */
    FEATURE,

    /** Related to the merging api
     * @since 1.0.4
     */
    MERGING,

    /** A core mechanic, disabling this will cause many things to stop working
     * @since 1.0.4
     */
    MECHANIC,

    /** Extreme means this has a Major impact on how pistons work and will likely break any contraption made without it being on
     * @since 1.0.4
     */
    EXTREME,

    /** An experimental config value, use with caution
     * @since 1.0.4
     */
    EXPERIMENTAL,

    /** An optimization config value, performance go brrrr
     * @since 1.0.4
     */
    OPTIMIZATION,

    /** Work In Progress, this is still missing some features
     * @since 1.0.4
     */
    WIP

}

package ca.fxco.api.pistonlib.config;

import net.fabricmc.api.EnvType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that this field is a Config Value
 * All config value fields must be static and not final
 * @author FX
 * @since 1.0.4
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigValue {

    /**
     * The config description, what does this config do?
     * @since 1.0.4
     */
    String desc() default "";

    /**
     * More information about the config
     * @since 1.0.4
     */
    String[] more() default {};

    /**
     * List of keywords that fit the config value
     * @since 1.0.4
     */
    String[] keyword() default {};

    /**
     * The categories that this config value fits into
     * @see Category
     * @since 1.0.4
     */
    Category[] category() default {};

    /**
     * Config options that are required in order to work
     * @since 1.0.4
     */
    String[] requires() default {};

    /**
     * Config options that conflict with each other
     * @since 1.0.4
     */
    String[] conflict() default {};

    /**
     * If the config option requires a restart to work
     * @since 1.0.4
     */
    boolean requiresRestart() default false;

    /**
     * If this config value fixes a vanilla bug, you can set the bug id's it fixes here
     * Just a default mojira id without the `MC-`
     * @since 1.0.4
     */
    int[] fixes() default {};

    /**
     * Checks multiple conditions before loading the config option into the config manager
     * @see Condition
     * @since 1.0.4
     */
    Class<? extends Condition>[] condition() default {};

    /**
     * This class will make sure that the config value is valid, and will convert string inputs to a valid type.
     * @see Parser
     * @since 1.0.4
     */
    Class<? extends Parser>[] parser() default {};

    /**
     * On which side should option be loaded.
     * @since 1.0.4
     */
    EnvType[] envType() default {EnvType.SERVER, EnvType.CLIENT};

    /**
     * The class of the condition checked when the rule is parsed, before being added
     * to the Settings Manager.
     * @see Observer
     * @since 1.0.4
     */
    Class<? extends Observer>[] observer() default {};

    /**
     * Things to suggest in config command
     *
     * @since 1.0.4
     */
    String[] suggestions() default {};

}

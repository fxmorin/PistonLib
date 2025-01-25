package ca.fxco.api.pistonlib.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply this annotation to a field, to specify what values should be tested.
 * Only specify values matching the type of the field you're applying this annotation to.
 * </br>
 * If the type of your value doesn't exist. Use {@link #stringValues}, which will attempt
 * to parse your values from strings.
 * </br>
 * If you use the annotation but don't set any values. Only the default value will be used
 * during testing. Except if it's a boolean, both true and false will be used.
 * This is the same behavior when the annotation isn't specified
 *
 * @author FX
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestValues {

    /**
     * Specify strings to test.
     * Unlike other values, stringValues accepts all types that aren't specified.
     * It'll attempt to parse the value from the strings.
     *
     * @return string values to test
     */
    String[] stringValues() default {};

    /**
     * Specify booleans to test.
     *
     * @return boolean values to test
     */
    boolean[] booleanValues() default {true, false};

    /**
     * Specify chars to test.
     *
     * @return char values to test
     */
    char[] charValues() default {};

    /**
     * Specify bytes to test.
     *
     * @return byte values to test
     */
    byte[] byteValues() default {};

    /**
     * Specify shorts to test.
     *
     * @return short values to test
     */
    short[] shortValues() default {};

    /**
     * Specify integers to test.
     *
     * @return integer values to test
     */
    int[] intValues() default {};

    /**
     * Specify longs to test.
     *
     * @return long values to test
     */
    long[] longValues() default {};

    /**
     * Specify floats to test.
     *
     * @return float values to test
     */
    float[] floatValues() default {};

    /**
     * Specify doubles to test.
     *
     * @return double values to test
     */
    double[] doubleValues() default {};
}

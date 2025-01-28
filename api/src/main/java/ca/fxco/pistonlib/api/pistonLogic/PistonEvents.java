package ca.fxco.pistonlib.api.pistonLogic;

/**
 * Constants representing different block event types used by pistons, as well
 * as helper methods for identifying the kind of motion they represent.
 *
 * @author Space Walker
 * @since 1.0.4
 */
public class PistonEvents {

    /**
     * Cancel the block event.
     *
     * @since 1.0.4
     */
    public static final int NONE            = -1;

    /**
     * Extend and push any blocks in front.
     *
     * @since 1.0.4
     */
    public static final int EXTEND          = 0;

    /**
     * Retract and pull any blocks in front.
     *
     * @since 1.0.4
     */
    public static final int RETRACT         = 1;

    /**
     * Retract without pulling any blocks.
     *
     * @since 1.0.4
     */
    public static final int RETRACT_NO_PULL = 2;

    /**
     * Check if an event is extending.
     *
     * @param type block event type to compare
     * @return {@code true} if type represents extension, otherwise {@code false}
     * @since 1.0.4
     */
    public static boolean isExtend(int type) {
        return type == EXTEND;
    }

    /**
     * Check if an event is retracting.
     *
     * @param type block event type to compare
     * @return {@code true} if type represents retraction, otherwise {@code false}
     * @since 1.0.4
     */
    public static boolean isRetract(int type) {
        return type == RETRACT || type == RETRACT_NO_PULL;
    }

    /**
     * Get the string representation of the type.
     *
     * @param type block event type to convert to string
     * @return string value of piston event type
     * @since 1.0.4
     */
    public static String toString(int type) {
        return switch (type) {
            case NONE -> "NONE";
            case EXTEND -> "EXTEND";
            case RETRACT -> "RETRACT";
            case RETRACT_NO_PULL -> "RETRACT_NO_PULL";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}

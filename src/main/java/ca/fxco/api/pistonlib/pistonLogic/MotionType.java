package ca.fxco.api.pistonlib.pistonLogic;

/**
 * Interface with useful fields and methods for motion type
 * @author Space Walker
 */
public class MotionType {

    /**
     * Cancel the motion from happening
     * @since 1.0.4
     */
    public static final int NONE    = -1;
    /**
     * Extend and push any blocks in front.
     * @since 1.0.4
     */
    public static final int PUSH    = 0;
    /**
     * Retract and pull any blocks in front.
     * @since 1.0.4
     */
    public static final int PULL    = 1;
    /**
     * Retract without pulling any blocks.
     * @since 1.0.4
     */
    public static final int RETRACT = 2;

    /**
     * @param type motion type to compare
     * @return true if type is equal push
     * @since 1.0.4
     */
    public static boolean isExtend(int type) {
        return type == PUSH;
    }

    /**
     * @param type motion type to compare
     * @return true if type is equal Retract or Pull
     * @since 1.0.4
     */
    public static boolean isRetract(int type) {
        return type == PULL || type == RETRACT;
    }

    /**
     * @param type motion type to convert to string
     * @return string value of motion type
     * @since 1.0.4
     */
    public static String toString(int type) {
        return switch (type) {
            case -1 -> "NONE";
            case 0 -> "PUSH";
            case 1 -> "PULL";
            case 2 -> "RETRACT";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}

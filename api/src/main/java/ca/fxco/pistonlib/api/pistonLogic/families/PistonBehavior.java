package ca.fxco.pistonlib.api.pistonLogic.families;

//TODO-API: JavaDoc
public interface PistonBehavior {

    boolean isVerySticky();

    boolean isFrontPowered();

    boolean isSlippery();

    boolean isQuasi();

    int getPushLimit();

    float getExtendingSpeed();

    float getRetractingSpeed();

    boolean isRetractOnExtending();

    boolean isExtendOnRetracting();

    int getMinLength();

    int getMaxLength();

    interface Builder {

        Builder verySticky();

        Builder frontPowered();

        Builder slippery();

        Builder noQuasi();

        Builder pushLimit(int limit);

        Builder speed(float generalSpeed);

        Builder speed(float extendingSpeed, float retractingSpeed);

        Builder extendingSpeed(float extendingSpeed);

        Builder retractingSpeed(float retractingSpeed);

        Builder retractOnExtending(boolean retractOnExtending);

        Builder extendOnRetracting(boolean extendOnRetracting);

        Builder maxLength(int maxLength);

        Builder minLength(int minLength);

        PistonBehavior build();
    }
}

package ca.fxco.pistonlib.api.pistonLogic;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper of {@link net.minecraft.world.level.material.PushReaction PushReaction}
 * that includes {@code default}, to be used in the `/pistonlib behavior` command.
 *
 * @author FX
 * @since 1.1.0
 */
public enum PistonMoveBehavior {

    DEFAULT  (0, "default"  , null), // Use vanilla behavior, no overrides
    NORMAL   (1, "normal"   , PushReaction.NORMAL),
    DESTROY  (2, "destroy"  , PushReaction.DESTROY),
    BLOCK    (3, "block"    , PushReaction.BLOCK),
    IGNORE   (4, "ignore"   , PushReaction.IGNORE),
    PUSH_ONLY(5, "push_only", PushReaction.PUSH_ONLY);
    // TODO: Somehow add pull_only support

    public static final StreamCodec<ByteBuf, PistonMoveBehavior> STREAM_CODEC =
            ByteBufCodecs.idMapper(PistonMoveBehavior::fromIndex, PistonMoveBehavior::getIndex);

    public static final PistonMoveBehavior[] ALL;
    private static final Map<String, PistonMoveBehavior> BY_NAME;
    private static final Map<PushReaction, PistonMoveBehavior> BY_PUSH_REACTION;

    private final int index;
    private final String name;
    private final PushReaction pushReaction;

    PistonMoveBehavior(int index, String name, PushReaction pushReaction) {
        this.index = index;
        this.name = name;
        this.pushReaction = pushReaction;
    }

    /**
     * Get the move behavior index.
     *
     * @return An integer representing the index
     * @since 1.1.0
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get the move behavior name.
     *
     * @return The name of the move behavior
     * @since 1.1.0
     */
    public String getName() {
        return name;
    }

    /**
     * Get the push reaction.
     *
     * @return The vanilla push reaction, this is {@code null} if using {@link #DEFAULT}
     * @since 1.1.0
     */
    public @Nullable PushReaction getPushReaction() {
        return pushReaction;
    }

    /**
     * Checks if the move behavior is present.
     * If it's present, it means we probably aren't using the default move behavior.
     *
     * @return {@code true} if the behavior is present, otherwise {@code false}
     * @since 1.1.0
     */
    public boolean isPresent() {
        return pushReaction != null;
    }

    /**
     * Checks if the move behavior matches a specific push reaction.
     *
     * @param pushReaction the push reaction to match
     * @return {@code true} if the behavior matches, otherwise {@code false}
     * @since 1.1.0
     */
    public boolean is(PushReaction pushReaction) {
        return pushReaction != null && this.pushReaction == pushReaction;
    }

    /**
     * Gets the move behavior from the index.
     *
     * @param index the index to find
     * @return The matching move behavior, or {@code null} if no behavior uses this index.
     * @since 1.1.0
     */
    public static @Nullable PistonMoveBehavior fromIndex(int index) {
        return (index < 0 || index >= ALL.length) ? null : ALL[index];
    }

    /**
     * Gets the move behavior from the name.
     *
     * @param name the name to find
     * @return The matching move behavior, or {@code null} if no behavior uses this name.
     * @since 1.1.0
     */
    public static @Nullable PistonMoveBehavior fromName(String name) {
        return name == null ? null : BY_NAME.get(name);
    }

    /**
     * Gets the move behavior from the push reaction.
     *
     * @param pushReaction the push reaction to find
     * @return The matching move behavior, or {@code null} if no behavior uses this push reaction.
     * @since 1.1.0
     */
    public static @Nullable PistonMoveBehavior fromPushReaction(PushReaction pushReaction) {
        return BY_PUSH_REACTION.get(pushReaction);
    }

    static {
        PistonMoveBehavior[] values = values();

        ALL = new PistonMoveBehavior[values.length];
        BY_NAME = new HashMap<>();
        BY_PUSH_REACTION = new HashMap<>();

        for (PistonMoveBehavior behavior : values) {
            ALL[behavior.index] = behavior;
            BY_NAME.put(behavior.name, behavior);

            if (behavior.pushReaction != null) {
                BY_PUSH_REACTION.put(behavior.pushReaction, behavior);
            }
        }
    }
}
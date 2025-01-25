package ca.fxco.api.pistonlib.pistonLogic;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.PushReaction;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper of {@link net.minecraft.world.level.material.PushReaction PushReaction}
 * that includes {@code default}, to be used in the `/pistonlib behavior` command.
 */
@Getter
@AllArgsConstructor
public enum PistonMoveBehavior {

    DEFAULT  (0, "default"  , null), // Use vanilla behavior, no overrides
    NORMAL   (1, "normal"   , PushReaction.NORMAL),
    DESTROY  (2, "destroy"  , PushReaction.DESTROY),
    BLOCK    (3, "block"    , PushReaction.BLOCK),
    IGNORE   (4, "ignore"   , PushReaction.IGNORE),
    PUSH_ONLY(5, "push_only", PushReaction.PUSH_ONLY);
    // TODO: Somehow add pull_only support

    public static final PistonMoveBehavior[] ALL;
    private static final Map<String, PistonMoveBehavior> BY_NAME;
    private static final Map<PushReaction, PistonMoveBehavior> BY_PUSH_REACTION;

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

    private final int index;
    private final String name;
    private final PushReaction pushReaction;

    public static PistonMoveBehavior fromIndex(int index) {
        return (index < 0 || index >= ALL.length) ? null : ALL[index];
    }

    public static final StreamCodec<ByteBuf, PistonMoveBehavior> STREAM_CODEC =
            ByteBufCodecs.idMapper(PistonMoveBehavior::fromIndex, PistonMoveBehavior::getIndex);

    public static PistonMoveBehavior fromName(String name) {
        return name == null ? null : BY_NAME.get(name);
    }

    public static PistonMoveBehavior fromPushReaction(PushReaction pushReaction) {
        return BY_PUSH_REACTION.get(pushReaction);
    }

    public boolean isPresent() {
        return pushReaction != null;
    }

    public boolean is(PushReaction pushReaction) {
        return pushReaction != null && this.pushReaction == pushReaction;
    }
}
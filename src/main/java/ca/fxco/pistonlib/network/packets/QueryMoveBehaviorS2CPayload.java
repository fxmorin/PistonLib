package ca.fxco.pistonlib.network.packets;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.api.pistonLogic.PistonMoveBehavior;
import ca.fxco.pistonlib.network.PLServerNetwork;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This is sent to the client after the client sends a {@link QueryMoveBehaviorC2SPayload}.
 * </br>
 * To get the query response, use the event {@link QueryMoveBehaviorCallback#EVENT}
 */
public record QueryMoveBehaviorS2CPayload(BlockState state, PistonMoveBehavior behavior) implements PLPayload {

    public static final CustomPacketPayload.Type<QueryMoveBehaviorS2CPayload> TYPE =
            new Type<>(PistonLib.id("query_move_behavior"));

    public static final StreamCodec<RegistryFriendlyByteBuf, QueryMoveBehaviorS2CPayload> STREAM_CODEC =
            StreamCodec.composite(
                    PLServerNetwork.BLOCKSTATE_STREAM_CODEC,
                    QueryMoveBehaviorS2CPayload::state,
                    PistonMoveBehavior.STREAM_CODEC,
                    QueryMoveBehaviorS2CPayload::behavior,
                    QueryMoveBehaviorS2CPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handleClient(PacketSender packetSender) {
        QueryMoveBehaviorCallback.EVENT.invoker().handle(state, behavior);
    }

    public interface QueryMoveBehaviorCallback {

        Event<QueryMoveBehaviorCallback> EVENT = EventFactory.createArrayBacked(QueryMoveBehaviorCallback.class,
                listeners -> (state, behavior) -> {
                    for (QueryMoveBehaviorCallback listener : listeners) {
                        listener.handle(state, behavior);
                    }
                });

        void handle(BlockState state, PistonMoveBehavior behavior);
    }
}

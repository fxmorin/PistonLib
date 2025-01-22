package ca.fxco.pistonlib.network.packets;

import ca.fxco.api.pistonlib.pistonLogic.PistonMoveBehavior;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.helpers.PistonLibBehaviorManager;
import ca.fxco.pistonlib.network.PLServerNetwork;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Request Query behavior information from the server.
 * The server sends you the result of your query through {@link ClientboundQueryMoveBehaviorPacket}
 */
public record ServerboundQueryMoveBehaviorPacket(BlockState state) implements PLPacket {

    public static final CustomPacketPayload.Type<ServerboundQueryMoveBehaviorPacket> TYPE =
            new Type<>(PistonLib.id("query_move_behavior_c2s"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundQueryMoveBehaviorPacket> STREAM_CODEC =
            StreamCodec.composite(
                    PLServerNetwork.BLOCKSTATE_STREAM_CODEC,
                    ServerboundQueryMoveBehaviorPacket::state,
                    ServerboundQueryMoveBehaviorPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handleServer(MinecraftServer server, ServerPlayer fromPlayer, PacketSender packetSender) {
        PistonMoveBehavior behavior = PistonLibBehaviorManager.getOverride(state);
        PLServerNetwork.sendToClient(fromPlayer, new ClientboundQueryMoveBehaviorPacket(state, behavior));
    }
}

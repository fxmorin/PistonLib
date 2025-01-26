package ca.fxco.pistonlib.network;

import ca.fxco.pistonlib.network.packets.*;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PLServerNetwork {

    public static final StreamCodec<ByteBuf, BlockState> BLOCKSTATE_STREAM_CODEC =
            ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Block> BLOCK_STREAM_CODEC =
            ByteBufCodecs.registry(Registries.BLOCK);

    public static void initialize() {
        //client to server
        registerServerBound(QueryMoveBehaviorC2SPayload.TYPE, QueryMoveBehaviorC2SPayload.STREAM_CODEC);

        //server to client
        registerClientBound(PistonEventS2CPayload.TYPE, PistonEventS2CPayload.STREAM_CODEC);
        registerClientBound(ModifyConfigS2CPayload.TYPE, ModifyConfigS2CPayload.STREAM_CODEC);
        registerClientBound(QueryMoveBehaviorS2CPayload.TYPE, QueryMoveBehaviorS2CPayload.STREAM_CODEC);
    }

    //
    // Registering Packets
    //

    private static <T extends PLPayload> void registerServerBound(CustomPacketPayload.Type<T> type,
                                                                  StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playC2S().register(type, streamCodec);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            MinecraftServer server = context.server();
            server.execute(() -> payload.handleServer(server, context.player(), context.responseSender()));
        });
    }

    public static <T extends PLPayload> void registerClientBound(CustomPacketPayload.Type<T> type,
                                                                 StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playS2C().register(type, streamCodec);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            PLClientNetwork.registerClientBound(type);
        }
    }

    //
    // Sending Packets
    //

    public static void sendToClient(ServerPlayer player, PLPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendToClients(List<ServerPlayer> players, PLPayload payload) {
        if (players.isEmpty()) {
            return;
        }
        for (ServerPlayer player : players) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public static void sendToAllClients(MinecraftServer server, PLPayload payload) {
        sendToClients(server.getPlayerList().getPlayers(), payload);
    }

    // Sends packets to all clients except the player hosting the world
    public static void sendToAllExternalClients(MinecraftServer server, PLPayload payload) {
        if (server.isDedicatedServer()) { // Server is independent
            sendToAllClients(server, payload);
        } else { // Filter out the host, and send to all others
            GameProfile gameProfile = server.getSingleplayerProfile();
            if (gameProfile == null) {
                sendToAllClients(server, payload);
                return;
            }
            String profileName = gameProfile.getName();
            List<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
            players.removeIf(p -> profileName.equalsIgnoreCase(p.getGameProfile().getName()));
            sendToClients(players, payload);
        }
    }

    public static void sendToClientsInRange(MinecraftServer server, GlobalPos fromPos,
                                            PLPayload payload, double distance) {
        BlockPos pos = fromPos.pos();
        ResourceKey<Level> dimensionKey = fromPos.dimension();
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (serverPlayer.level().dimension() == dimensionKey &&
                    pos.closerToCenterThan(serverPlayer.position(), distance)) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }
        }
    }

    public static void sendToClientsInRange(MinecraftServer server, GlobalPos fromPos, PLPayload payload,
                                            double distance, @Nullable ServerPlayer exclude) {
        BlockPos pos = fromPos.pos();
        ResourceKey<Level> dimensionKey = fromPos.dimension();
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (serverPlayer != exclude && serverPlayer.level().dimension() == dimensionKey &&
                    pos.closerToCenterThan(serverPlayer.position(), distance)) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }
        }
    }

    public static void sendToClientsInRange(MinecraftServer server, GlobalPos fromPos, PLPayload payload,
                                            double distance, Predicate<ServerPlayer> predicate) {
        BlockPos pos = fromPos.pos();
        ResourceKey<Level> dimensionKey = fromPos.dimension();
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (serverPlayer.level().dimension() == dimensionKey &&
                    pos.closerToCenterThan(serverPlayer.position(), distance) && predicate.test(serverPlayer)) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }
        }
    }

}

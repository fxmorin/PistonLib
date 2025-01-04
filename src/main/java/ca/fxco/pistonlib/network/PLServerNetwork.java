package ca.fxco.pistonlib.network;

import ca.fxco.pistonlib.helpers.Utils;
import ca.fxco.pistonlib.network.packets.*;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PLServerNetwork {

    private static final HashMap<Class<? extends PLPacket>, ResourceLocation> PACKET_TYPES = new HashMap<>();

    public static void initialize() {
        registerServerBound(ServerboundQueryMoveBehaviorPacket.ID, ServerboundQueryMoveBehaviorPacket.class);

        registerClientBound(ClientboundPistonEventPacket.ID, ClientboundPistonEventPacket.class);
        registerClientBound(ClientboundModifyConfigPacket.ID, ClientboundModifyConfigPacket.class);
        registerClientBound(ClientboundQueryMoveBehaviorPacket.ID, ClientboundQueryMoveBehaviorPacket.class);
    }

    //
    // Registering Packets
    //

    private static <T extends PLPacket> void registerServerBound(ResourceLocation id, Class<T> type) {
        registerServerBound(id, type, () -> Utils.createInstance(type));
    }

    private static <T extends PLPacket> void registerServerBound(ResourceLocation id, Class<T> type,
                                                                 Supplier<T> packetGen) {
        PACKET_TYPES.put(type, id);
        ServerPlayNetworking.registerGlobalReceiver(id, (server, player, listener, buf, packetSender) -> {
            T packet = packetGen.get();
            packet.read(buf);
            server.execute(() -> packet.handleServer(server, player, packetSender));
        });
    }

    public static <T extends PLPacket> void registerClientBound(ResourceLocation id, Class<T> type) {
        PACKET_TYPES.put(type, id);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            PLClientNetwork.registerClientBound(id, type);
        }
    }

    //
    // Sending Packets
    //

    public static void sendToClient(ServerPlayer player, PLPacket packet) {
        ResourceLocation id = getPacketId(packet);
        ServerPlayNetworking.send(player, id, packet.writeAsBuffer());
    }

    public static void sendToClients(List<ServerPlayer> players, PLPacket packet) {
        if (players.isEmpty()) {
            return;
        }
        ResourceLocation id = getPacketId(packet);
        FriendlyByteBuf buf = packet.writeAsBuffer();
        for (ServerPlayer player : players) {
            ServerPlayNetworking.send(player, id, buf);
        }
    }

    public static void sendToAllClients(MinecraftServer server, PLPacket packet) {
        sendToClients(server.getPlayerList().getPlayers(), packet);
    }

    // Sends packets to all clients except the player hosting the world
    public static void sendToAllExternalClients(MinecraftServer server, PLPacket packet) {
        if (server.isDedicatedServer()) { // Server is independent
            sendToAllClients(server, packet);
        } else { // Filter out the host, and send to all others
            GameProfile gameProfile = server.getSingleplayerProfile();
            if (gameProfile == null) {
                sendToAllClients(server, packet);
                return;
            }
            String profileName = gameProfile.getName();
            List<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
            players.removeIf(p -> profileName.equalsIgnoreCase(p.getGameProfile().getName()));
            sendToClients(players, packet);
        }
    }

    public static void sendToClientsInRange(MinecraftServer server, GlobalPos fromPos,
                                            PLPacket packet, double distance) {
        ResourceLocation id = getPacketId(packet);
        FriendlyByteBuf buf = null;
        BlockPos pos = fromPos.pos();
        ResourceKey<Level> dimensionKey = fromPos.dimension();
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (serverPlayer.level.dimension() == dimensionKey &&
                    pos.closerToCenterThan(serverPlayer.position(), distance)) {
                if (buf == null) { // Don't create packet if it doesn't get sent to anyone
                    buf = packet.writeAsBuffer();
                }
                ServerPlayNetworking.send(serverPlayer, id, buf);
            }
        }
    }

    public static void sendToClientsInRange(MinecraftServer server, GlobalPos fromPos, PLPacket packet,
                                            double distance, @Nullable ServerPlayer exclude) {
        ResourceLocation id = getPacketId(packet);
        FriendlyByteBuf buf = null;
        BlockPos pos = fromPos.pos();
        ResourceKey<Level> dimensionKey = fromPos.dimension();
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (serverPlayer != exclude && serverPlayer.level.dimension() == dimensionKey &&
                    pos.closerToCenterThan(serverPlayer.position(), distance)) {
                if (buf == null) { // Don't create packet if it doesn't get sent to anyone
                    buf = packet.writeAsBuffer();
                }
                ServerPlayNetworking.send(serverPlayer, id, buf);
            }
        }
    }

    public static void sendToClientsInRange(MinecraftServer server, GlobalPos fromPos, PLPacket packet,
                                            double distance, Predicate<ServerPlayer> predicate) {
        ResourceLocation id = getPacketId(packet);
        FriendlyByteBuf buf = null;
        BlockPos pos = fromPos.pos();
        ResourceKey<Level> dimensionKey = fromPos.dimension();
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (serverPlayer.level.dimension() == dimensionKey &&
                    pos.closerToCenterThan(serverPlayer.position(), distance) && predicate.test(serverPlayer)) {
                if (buf == null) { // Don't create packet if it doesn't get sent to anyone
                    buf = packet.writeAsBuffer();
                }
                ServerPlayNetworking.send(serverPlayer, id, buf);
            }
        }
    }

    //
    // Validation
    //

    public static ResourceLocation getPacketId(PLPacket packet) {
        ResourceLocation id = PACKET_TYPES.get(packet.getClass());
        if (id != null) {
            return id;
        }
        throw new IllegalArgumentException("Invalid packet type! - " + packet.getClass());
    }
}

package ca.fxco.pistonlib.network;

import ca.fxco.pistonlib.helpers.Utils;
import ca.fxco.pistonlib.network.packets.ClientboundModifyConfigPacket;
import ca.fxco.pistonlib.network.packets.ClientboundPistonEventPacket;
import ca.fxco.pistonlib.network.packets.PLPacket;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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

public class PLNetwork {

    // Clientbound = S2C
    // Serverbound = C2S

    private static final HashMap<Class<? extends PLPacket>, ResourceLocation> CLIENTBOUND_PACKET_TYPES = new HashMap<>();
    private static final HashMap<Class<? extends PLPacket>, ResourceLocation> SERVERBOUND_PACKET_TYPES = new HashMap<>();

    public static void initialize() {
        registerClientBound(ClientboundPistonEventPacket.ID, ClientboundPistonEventPacket.class);
        registerClientBound(ClientboundModifyConfigPacket.ID, ClientboundModifyConfigPacket.class);
    }

    //
    // Registering Packets
    //

    private static <T extends PLPacket> void registerClientBound(ResourceLocation id, Class<T> type) {
        registerClientBound(id, type, () -> Utils.createInstance(type));
    }

    private static <T extends PLPacket> void registerClientBound(ResourceLocation id, Class<T> type,
                                                                 Supplier<T> packetGen) {
        CLIENTBOUND_PACKET_TYPES.put(type, id);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, packetSender) -> {
                T packet = packetGen.get();
                packet.read(buf);
                client.execute(() -> packet.handleClient(client, packetSender));
            });
        }
    }

    private static <T extends PLPacket> void registerServerBound(ResourceLocation id, Class<T> type) {
        registerServerBound(id, type, () -> Utils.createInstance(type));
    }

    private static <T extends PLPacket> void registerServerBound(ResourceLocation id, Class<T> type,
                                                                 Supplier<T> packetGen) {
        SERVERBOUND_PACKET_TYPES.put(type, id);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            ServerPlayNetworking.registerGlobalReceiver(id, (server, player, listener, buf, packetSender) -> {
                T packet = packetGen.get();
                packet.read(buf);
                server.execute(() -> packet.handleServer(server, player, packetSender));
            });
        }
    }

    //
    // Sending Packets
    //

    @Environment(EnvType.CLIENT)
    public static void sendToServer(PLPacket packet) {
        ResourceLocation id = getPacketId(packet, EnvType.CLIENT);
        ClientPlayNetworking.send(id, packet.writeAsBuffer());
    }

    public static void sendToClient(ServerPlayer player, PLPacket packet) {
        ResourceLocation id = getPacketId(packet, EnvType.SERVER);
        ServerPlayNetworking.send(player, id, packet.writeAsBuffer());
    }

    public static void sendToClients(List<ServerPlayer> players, PLPacket packet) {
        if (players.isEmpty()) {
            return;
        }
        ResourceLocation id = getPacketId(packet, EnvType.SERVER);
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
        ResourceLocation id = getPacketId(packet, EnvType.SERVER);
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
        ResourceLocation id = getPacketId(packet, EnvType.SERVER);
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
        ResourceLocation id = getPacketId(packet, EnvType.SERVER);
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

    private static ResourceLocation getPacketId(PLPacket packet, EnvType envType) {
        ResourceLocation id = (envType == EnvType.SERVER ? CLIENTBOUND_PACKET_TYPES : SERVERBOUND_PACKET_TYPES)
                .get(packet.getClass());
        if (id != null) {
            return id;
        }
        // Used to create the exception to throw, gets the other list to check if its there
        ResourceLocation wrong = (envType != EnvType.SERVER ? CLIENTBOUND_PACKET_TYPES : SERVERBOUND_PACKET_TYPES)
                .get(packet.getClass());
        if (wrong != null) {
            throw new IllegalArgumentException(
                    (envType == EnvType.SERVER ?
                            "Cannot send C2S packet to clients - " : "Cannot send S2C packet to server - ") +
                            packet.getClass().getSimpleName()
            );
        } else {
            throw new IllegalArgumentException("Invalid packet type!");
        }
    }
}

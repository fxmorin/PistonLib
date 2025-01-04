package ca.fxco.pistonlib.network;

import ca.fxco.pistonlib.helpers.Utils;
import ca.fxco.pistonlib.network.packets.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class PLClientNetwork {

    private static final HashMap<Class<? extends PLPacket>, ResourceLocation> CLIENTBOUND_PACKET_TYPES = new HashMap<>();

    public static void initialize() {
        registerClientBound(ClientboundPistonEventPacket.ID, ClientboundPistonEventPacket.class);
        registerClientBound(ClientboundModifyConfigPacket.ID, ClientboundModifyConfigPacket.class);
        registerClientBound(ClientboundQueryMoveBehaviorPacket.ID, ClientboundQueryMoveBehaviorPacket.class);
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
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, packetSender) -> {
            T packet = packetGen.get();
            packet.read(buf);
            client.execute(() -> packet.handleClient(client, packetSender));
        });
    }

    //
    // Sending Packets
    //

    public static void sendToServer(PLPacket packet) {
        ClientPlayNetworking.send(getPacketId(packet), packet.writeAsBuffer());
    }

    //
    // Validation
    //

    private static ResourceLocation getPacketId(PLPacket packet) {
        ResourceLocation id = CLIENTBOUND_PACKET_TYPES.get(packet.getClass());
        if (id != null) {
            return id;
        }
        throw new IllegalArgumentException("Invalid packet type! - " + packet.getClass());
    }
}

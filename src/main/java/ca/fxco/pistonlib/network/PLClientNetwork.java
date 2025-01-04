package ca.fxco.pistonlib.network;

import ca.fxco.pistonlib.helpers.Utils;
import ca.fxco.pistonlib.network.packets.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class PLClientNetwork {

    //
    // Registering Packets
    //

    public static <T extends PLPacket> void registerClientBound(ResourceLocation id, Class<T> type) {
        registerClientBound(id, type, () -> Utils.createInstance(type));
    }

    public static <T extends PLPacket> void registerClientBound(ResourceLocation id, Class<T> type,
                                                                 Supplier<T> packetGen) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, packetSender) -> {
            T packet = packetGen.get();
            packet.read(buf);
            client.execute(() -> packet.handleClient(packetSender));
        });
    }

    //
    // Sending Packets
    //

    public static void sendToServer(PLPacket packet) {
        ClientPlayNetworking.send(PLServerNetwork.getPacketId(packet), packet.writeAsBuffer());
    }
}

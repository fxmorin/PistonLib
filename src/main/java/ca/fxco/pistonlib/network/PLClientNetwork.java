package ca.fxco.pistonlib.network;

import ca.fxco.pistonlib.network.packets.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@Environment(EnvType.CLIENT)
public class PLClientNetwork {

    //
    // Registering Packets
    //

    public static <T extends PLPayload> void registerClientBound(CustomPacketPayload.Type<T> type) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) ->
                context.client().execute(() -> payload.handleClient(context.responseSender())));
    }

    //
    // Sending Packets
    //

    public static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}

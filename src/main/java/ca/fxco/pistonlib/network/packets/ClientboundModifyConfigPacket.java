package ca.fxco.pistonlib.network.packets;

import ca.fxco.api.pistonlib.config.ParsedValue;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.config.ParsedValueImpl;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public record ClientboundModifyConfigPacket(Map<ParsedValue<?>, String> configValues) implements PLPacket {

    public static final Type<ClientboundModifyConfigPacket> TYPE = new Type<>(PistonLib.id("modify_config"));

    public static final StreamCodec<FriendlyByteBuf, ClientboundModifyConfigPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ParsedValueImpl.STREAM_CODEC,
                            ByteBufCodecs.STRING_UTF8,
                            256
                    ),
                    ClientboundModifyConfigPacket::configValues,
                    ClientboundModifyConfigPacket::new
            );

    public static ClientboundModifyConfigPacket fromCollection(Collection<ParsedValue<?>> configValues) {
        HashMap<ParsedValue<?>, String> values = new HashMap<>();
        for (ParsedValue<?> value : configValues) {
            // TODO: Filter out server-only values. We currently don't store the environment side in the parsed value!
            values.put(value, String.valueOf(value.getValue()));
        }

        return new ClientboundModifyConfigPacket(values);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handleClient(PacketSender packetSender) {
        for (var entry : configValues.entrySet()) {
            entry.getKey().parseValue(null, entry.getValue());
        }
    }
}

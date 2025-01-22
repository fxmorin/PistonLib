package ca.fxco.pistonlib.network.packets;

import ca.fxco.api.pistonlib.config.ParsedValue;
import ca.fxco.pistonlib.PistonLib;
import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public record ClientboundModifyConfigPacket(Map<ParsedValue, Object> configValues) implements PLPacket {

    public static final Type<ClientboundModifyConfigPacket> TYPE = new Type<>(PistonLib.id("modify_config"));

    @SuppressWarnings("rawtypes")
    public static final StreamCodec<FriendlyByteBuf, ClientboundModifyConfigPacket> STREAM_CODEC =
            StreamCodec.composite(
                    new StreamCodec<>() {
                        @Override
                        public Map<ParsedValue, Object> decode(FriendlyByteBuf buf) {
                            return PistonLib.getConfigManager().readValuesFromBuffer(buf);
                        }

                        @Override
                        public void encode(FriendlyByteBuf buf, Map<ParsedValue, Object> values) {
                            PistonLib.getConfigManager().writeValuesToBuffer(buf, values.keySet().toArray(new ParsedValue[0]));
                        }
                    },
                    ClientboundModifyConfigPacket::configValues,
                    ClientboundModifyConfigPacket::new
            );

    @SuppressWarnings("rawtypes")
    public static ClientboundModifyConfigPacket fromCollection(Collection<ParsedValue<?>> configValues) {
        HashMap<ParsedValue, Object> values = new HashMap<>();
        for (ParsedValue value : configValues) {
            // TODO: Filter out server-only values. We currently don't store the environment side in the parsed value!
            values.put(value, value);
        }

        return new ClientboundModifyConfigPacket(values);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleClient(PacketSender packetSender) {
        for (var entry : configValues.entrySet()) {
            entry.getKey().setValue(entry.getValue(), true);
        }
    }
}

package ca.fxco.pistonlib.network.packets;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.api.config.ParsedValue;
import ca.fxco.pistonlib.config.ParsedValueImpl;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public record ModifyConfigS2CPayload(Map<ParsedValue<?>, String> configValues) implements PLPayload {

    public static final Type<ModifyConfigS2CPayload> TYPE = new Type<>(PistonLib.id("modify_config"));

    public static final StreamCodec<FriendlyByteBuf, ModifyConfigS2CPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                            ParsedValueImpl.STREAM_CODEC,
                            ByteBufCodecs.STRING_UTF8,
                            256
                    ),
                    ModifyConfigS2CPayload::configValues,
                    ModifyConfigS2CPayload::new
            );

    public static ModifyConfigS2CPayload fromCollection(Collection<ParsedValue<?>> configValues) {
        HashMap<ParsedValue<?>, String> values = new HashMap<>();
        for (ParsedValue<?> value : configValues) {
            // TODO: Filter out server-only values. We currently don't store the environment side in the parsed value!
            values.put(value, String.valueOf(value.getValue()));
        }

        return new ModifyConfigS2CPayload(values);
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

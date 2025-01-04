package ca.fxco.pistonlib.network.packets;

import ca.fxco.api.pistonlib.config.ParsedValue;
import ca.fxco.pistonlib.PistonLib;
import lombok.NoArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ClientboundLoadConfigPacket extends PLPacket {

    public static ResourceLocation ID = PistonLib.id("load_config");

    @SuppressWarnings("rawtypes")
    private Map<ParsedValue, Object> configValues;

    @SuppressWarnings("rawtypes")
    public ClientboundLoadConfigPacket(Collection<ParsedValue<?>> configValues) {
        HashMap<ParsedValue, Object> values = new HashMap<>();
        for (ParsedValue value : configValues) {
            // TODO: Filter out server-only values. We currently don't store the environment side in the parsed value!
            values.put(value, value);
        }
        this.configValues = values;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        PistonLib.getConfigManager().writeValuesToBuffer(buf, this.configValues.keySet().toArray(new ParsedValue[0]));
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.configValues = PistonLib.getConfigManager().readValuesFromBuffer(buf);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleClient(Minecraft client, PacketSender packetSender) {
        for (var entry : configValues.entrySet()) {
            entry.getKey().setValue(entry.getValue(), true);
        }
    }
}

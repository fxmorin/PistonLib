package ca.fxco.pistonlib.network.packets;

import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import ca.fxco.pistonlib.helpers.PistonEventData;
import ca.fxco.pistonlib.network.ClientPacketHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PistonEventS2CPayload(
        BasicPistonBaseBlock pistonBlock,
        BlockPos pos,
        Direction dir,
        boolean extend
) implements PLPayload {

    public static final CustomPacketPayload.Type<PistonEventS2CPayload> TYPE =
            new Type<>(PistonLib.id("piston_event"));

    private static final StreamCodec<RegistryFriendlyByteBuf, BasicPistonBaseBlock> BLOCK_STREAM_CODEC =
            ByteBufCodecs.registry(Registries.BLOCK).map(
                    block -> (BasicPistonBaseBlock) block, b -> b);

    public static final StreamCodec<RegistryFriendlyByteBuf, PistonEventS2CPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BLOCK_STREAM_CODEC,
                    PistonEventS2CPayload::pistonBlock,
                    BlockPos.STREAM_CODEC,
                    PistonEventS2CPayload::pos,
                    Direction.STREAM_CODEC,
                    PistonEventS2CPayload::dir,
                    ByteBufCodecs.BOOL,
                    PistonEventS2CPayload::extend,
                    PistonEventS2CPayload::new
            );

    public PistonEventS2CPayload(PistonEventData pistonEventData) {
        this(pistonEventData.pistonBlock(), pistonEventData.pos(), pistonEventData.dir(), pistonEventData.extend());
    }

    @Override
    public void handleClient(PacketSender packetSender) {
        ClientPacketHandler.handle(this, packetSender);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

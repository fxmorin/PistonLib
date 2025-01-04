package ca.fxco.pistonlib.network.packets;

import ca.fxco.api.pistonlib.pistonLogic.PistonMoveBehavior;
import ca.fxco.pistonlib.helpers.PistonLibBehaviorManager;
import ca.fxco.pistonlib.network.PLNetwork;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Request Query behavior information from the server.
 * The server sends you the result of your query through {@link ClientboundQueryMoveBehaviorPacket}
 */
@AllArgsConstructor
@NoArgsConstructor
public class ServerboundQueryMoveBehaviorPacket extends PLPacket {

    private BlockState state;

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeId(Block.BLOCK_STATE_REGISTRY, this.state);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.state = buf.readById(Block.BLOCK_STATE_REGISTRY);
    }

    @Override
    public void handleServer(MinecraftServer server, ServerPlayer fromPlayer, PacketSender packetSender) {
        PistonMoveBehavior behavior = PistonLibBehaviorManager.getOverride(state);
        PLNetwork.sendToClient(fromPlayer, new ClientboundQueryMoveBehaviorPacket(state, behavior));
    }
}

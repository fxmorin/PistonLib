package ca.fxco.pistonlib.network.packets;

import ca.fxco.api.pistonlib.pistonLogic.PistonMoveBehavior;
import ca.fxco.pistonlib.PistonLib;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This is sent to the client after the client sends a {@link ServerboundQueryMoveBehaviorPacket}.
 * </br>
 * To get the query response, use the event {@link QueryMoveBehaviorCallback#EVENT}
 */
@AllArgsConstructor
@NoArgsConstructor
public class ClientboundQueryMoveBehaviorPacket extends PLPacket {

    public static ResourceLocation ID = PistonLib.id("query_move_behavior");

    private BlockState state;
    private PistonMoveBehavior behavior;

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeId(Block.BLOCK_STATE_REGISTRY, this.state);
        buf.writeEnum(behavior);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.state = buf.readById(Block.BLOCK_STATE_REGISTRY);
        this.behavior = buf.readEnum(PistonMoveBehavior.class);
    }

    @Override
    public void handleClient(Minecraft client, PacketSender packetSender) {
        QueryMoveBehaviorCallback.EVENT.invoker().handle(state, behavior);
    }

    public interface QueryMoveBehaviorCallback {

        Event<QueryMoveBehaviorCallback> EVENT = EventFactory.createArrayBacked(QueryMoveBehaviorCallback.class,
                listeners -> (state, behavior) -> {
                    for (QueryMoveBehaviorCallback listener : listeners) {
                        listener.handle(state, behavior);
                    }
                });

        void handle(BlockState state, PistonMoveBehavior behavior);
    }
}

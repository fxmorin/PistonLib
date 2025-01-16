package ca.fxco.pistonlib.network;

import ca.fxco.api.pistonlib.pistonLogic.controller.PistonController;
import ca.fxco.pistonlib.network.packets.ClientboundPistonEventPacket;
import ca.fxco.pistonlib.pistonLogic.structureRunners.DecoupledStructureRunner;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;

public class ClientPacketHandler {

    public static void handle(ClientboundPistonEventPacket packet, PacketSender packetSender) {
        PistonController controller = packet.getPistonBlock().pl$getPistonController();
        new DecoupledStructureRunner(controller.newStructureRunner(
                Minecraft.getInstance().level,
                packet.getPos(),
                packet.getDir(),
                1,
                packet.isExtend(),
                controller::newStructureResolver
        )).run();
    }

}

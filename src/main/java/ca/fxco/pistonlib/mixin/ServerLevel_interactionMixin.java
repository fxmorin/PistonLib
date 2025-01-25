package ca.fxco.pistonlib.mixin;

import ca.fxco.pistonlib.api.pistonLogic.controller.PistonController;
import ca.fxco.pistonlib.api.pistonLogic.structure.StructureRunner;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import ca.fxco.pistonlib.helpers.PistonEventData;
import ca.fxco.pistonlib.network.PLServerNetwork;
import ca.fxco.pistonlib.network.packets.PistonEventS2CPayload;
import ca.fxco.pistonlib.pistonLogic.structureRunners.DecoupledStructureRunner;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevel_interactionMixin extends Level {

    private ServerLevel_interactionMixin(WritableLevelData data, ResourceKey<Level> key,
                                         RegistryAccess registryAccess, Holder<DimensionType> dimension,
                                         boolean isClientSide, boolean isDebug, long seed,
                                         int maxChainedNeighborUpdates) {
        super(data, key, registryAccess, dimension, isClientSide, isDebug, seed, maxChainedNeighborUpdates);
    }

    @Unique
    private final Set<PistonEventData> pl$pistonEvents = new HashSet<>();

    @Override
    public void pl$addPistonEvent(BasicPistonBaseBlock pistonBase, BlockPos pos, Direction dir, boolean extend) {
        this.pl$pistonEvents.add(new PistonEventData(pistonBase, pos, dir, extend));
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;runBlockEvents()V"
            )
    )
    private void afterBlockEvents(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        pl$runPistonEvents();
    }

    private void pl$runPistonEvents() {
        Set<PistonEventData> runningPistonEvents = new HashSet<>(this.pl$pistonEvents);
        this.pl$pistonEvents.clear();
        for (PistonEventData pistonEvent : runningPistonEvents) {
            BasicPistonBaseBlock pistonBase = pistonEvent.pistonBlock();
            PistonController controller = pistonBase.pl$getPistonController();
            StructureRunner structureRunner = new DecoupledStructureRunner(controller.newStructureRunner(
                    this,
                    pistonEvent.pos(),
                    pistonEvent.dir(),
                    1, // Can't use length in decoupled piston logic
                    pistonEvent.extend(),
                    controller::newStructureResolver
            ));
            if (structureRunner.run()) {
                PLServerNetwork.sendToClientsInRange(
                        this.getServer(),
                        GlobalPos.of(this.dimension(), pistonEvent.pos()),
                        new PistonEventS2CPayload(pistonEvent),
                        64
                );
            }
        }
    }
}

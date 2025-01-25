package ca.fxco.pistonlib.mixin;

import ca.fxco.api.pistonlib.pistonLogic.controller.PistonController;
import ca.fxco.pistonlib.PistonLibConfig;
import ca.fxco.pistonlib.base.ModTags;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicPistonBaseBlock;
import ca.fxco.pistonlib.helpers.PistonEventData;
import ca.fxco.pistonlib.network.PLServerNetwork;
import ca.fxco.pistonlib.network.packets.PistonEventS2CPayload;
import ca.fxco.pistonlib.pistonLogic.structureRunners.DecoupledStructureRunner;
import ca.fxco.api.pistonlib.pistonLogic.structure.StructureRunner;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockEventData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

@Mixin(value = ServerLevel.class, priority = 1500) // Lower priority so that its after g4mespeed & tis
public abstract class ServerLevelMixin extends Level {

    private ServerLevelMixin(WritableLevelData data, ResourceKey<Level> key,
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
    private void pl$afterBlockEvents(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        pl$runPistonEvents();
    }

    @Unique
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
                        PistonLibConfig.pistonBlockEventDistance
                );
            }
        }
    }

    @ModifyArg(
            method = "runBlockEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcast(" +
                            "Lnet/minecraft/world/entity/player/Player;DDDDLnet/minecraft/resources/ResourceKey;" +
                            "Lnet/minecraft/network/protocol/Packet;)V"
            ),
            index = 4
    )
    private double pl$changePistonBlockEventRange(double d, @Local BlockEventData blockEventData) {
        if (PistonLibConfig.pistonBlockEventDistance != 64 &&
                blockEventData.block().builtInRegistryHolder().is(ModTags.PISTONS)) { // Only affect pistons
            return PistonLibConfig.pistonBlockEventDistance;
        }
        return d;
    }
}

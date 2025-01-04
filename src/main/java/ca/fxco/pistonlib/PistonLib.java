package ca.fxco.pistonlib;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

import ca.fxco.api.pistonlib.PistonLibInitializer;
import ca.fxco.api.pistonlib.config.ConfigFieldEntrypoint;
import ca.fxco.api.pistonlib.config.ConfigManager;
import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyGroups;
import ca.fxco.pistonlib.base.*;
import ca.fxco.api.pistonlib.config.ConfigManagerEntrypoint;
import ca.fxco.pistonlib.helpers.PistonLibBehaviorManager;
import ca.fxco.pistonlib.network.PLNetwork;
import ca.fxco.pistonlib.network.packets.ClientboundModifyConfigPacket;
import lombok.Getter;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;

import net.minecraft.resources.ResourceLocation;

public class PistonLib implements ModInitializer, PistonLibInitializer {

    public static final String MOD_ID = "pistonlib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final boolean DATAGEN_ACTIVE = System.getProperty("fabric-api.datagen") != null;

    @Getter
    private static final ConfigManager configManager = new ConfigManager(MOD_ID, PistonLibConfig.class);

    @Getter
    private static Optional<MinecraftServer> server = Optional.empty();

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModRegistries.bootstrap();

        initialize(PistonLibInitializer::registerPistonFamilies);
        initialize(PistonLibInitializer::registerStickyGroups);
        initialize(PistonLibInitializer::bootstrap);

        ModPistonFamilies.validate();
        ModStickyGroups.validate();

        PLNetwork.initialize();

        Map<String, List<Field>> customParsedValues = new HashMap<>();
        for (EntrypointContainer<ConfigFieldEntrypoint> entrypointContainer : FabricLoader.getInstance()
                .getEntrypointContainers("pistonlib-configfield", ConfigFieldEntrypoint.class)) {
            entrypointContainer.getEntrypoint().getConfigFields().forEach((key, value) ->
                    customParsedValues.computeIfAbsent(key, string -> new ArrayList<>()).addAll(value));
        }

        for (EntrypointContainer<ConfigManagerEntrypoint> entrypointContainer : FabricLoader.getInstance()
                .getEntrypointContainers("pistonlib-configmanager", ConfigManagerEntrypoint.class)) {
            entrypointContainer.getEntrypoint().getConfigManager().init(
                    entrypointContainer.getProvider().getMetadata().getId(),
                    customParsedValues
            );
        }
        PistonLibBehaviorManager.load();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // Send all options on player join, so that our configs match up
            // TODO: Should send only the non-default options. However this will result in version mismatch issues.
            PLNetwork.sendToClient(
                    handler.player,
                    new ClientboundModifyConfigPacket(PistonLib.getConfigManager().getParsedValues())
            );
        });
    }

    public static void onStartServer(MinecraftServer s) {
        server = Optional.of(s);
        PistonLib.getConfigManager().initializeConfig();
    }

    public static void onStopServer() {
        server = Optional.empty();
        PistonLibBehaviorManager.save(false);
    }

    @Override
    public void registerPistonFamilies() {
        ModPistonFamilies.bootstrap();
    }

    @Override
    public void registerStickyGroups() {
        ModStickyGroups.bootstrap();
        StickyGroups.bootstrap();
    }

    @Override
    public void bootstrap() {
        ModBlocks.bootstrap();
        ModBlockEntities.bootstrap();
        ModItems.boostrap();
        ModCreativeModeTabs.bootstrap();
        ModMenus.boostrap();
        ModScreens.boostrap();
        ModArgumentTypes.bootstrap();
        ModCommands.bootstrap();
    }

    private void initialize(Consumer<PistonLibInitializer> invoker) {
        EntrypointUtils.invoke(MOD_ID, PistonLibInitializer.class, invoker);
    }
}

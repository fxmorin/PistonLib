package ca.fxco.pistonlib;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

import ca.fxco.pistonlib.api.PistonLibApi;
import ca.fxco.pistonlib.api.PistonLibInitializer;
import ca.fxco.pistonlib.api.PistonLibSupplier;
import ca.fxco.pistonlib.api.config.ConfigFieldEntrypoint;
import ca.fxco.pistonlib.api.config.ConfigManager;
import ca.fxco.pistonlib.api.config.ConfigManagerEntrypoint;
import ca.fxco.pistonlib.base.*;
import ca.fxco.pistonlib.config.ConfigManagerImpl;
import ca.fxco.pistonlib.helpers.PistonLibBehaviorManager;
import ca.fxco.pistonlib.network.PLServerNetwork;
import ca.fxco.pistonlib.network.packets.ModifyConfigS2CPayload;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlagUniverse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

public class PistonLib implements ModInitializer, PistonLibInitializer, PistonLibSupplier {

    public static final String MOD_ID = "pistonlib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final boolean DATAGEN_ACTIVE = System.getProperty("fabric-api.datagen") != null;
    public static final Direction[] DIRECTIONS = Direction.values();
    public static final FeatureFlagSet NEVER_ENABLED_SET = FeatureFlagSet.of(
            new FeatureFlag(new FeatureFlagUniverse("fake_universe"), 64)
    );

    @Getter
    private static final ConfigManager configManager = new ConfigManagerImpl(MOD_ID, PistonLibConfig.class);

    @Getter
    private static Optional<MinecraftServer> server = Optional.empty();

    @Override
    public void onInitialize() {
        PistonLibApi.setSupplier(this);

        initialize(p -> p.initialize());
        initialize(PistonLibInitializer::bootstrap);
        initialize(PistonLibInitializer::registerPistonFamilies);
        initialize(PistonLibInitializer::registerStickyGroups);

        ModPistonFamilies.validate();
        ModStickyGroups.validate();

        PLServerNetwork.initialize();

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
            PLServerNetwork.sendToClient(
                    handler.player,
                    ModifyConfigS2CPayload.fromCollection(PistonLib.getConfigManager().getParsedValues())
            );
        });
    }

    @Override
    public void initialize() {}

    @Override
    public void registerPistonFamilies() {
        ModPistonFamilies.bootstrap();
    }

    @Override
    public void registerStickyGroups() {
        ModStickyGroups.bootstrap();
    }

    @Override
    public void bootstrap() {
        ModBlocks.bootstrap();
        ModBlockEntities.bootstrap();
        ModItems.bootstrap();
        ModDataComponents.bootstrap();
        ModMenus.bootstrap();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ModCreativeModeTabs.bootstrap();
            ModScreens.bootstrap();
        }
        ModArgumentTypes.bootstrap();
        ModCommands.bootstrap();
    }

    private void initialize(Consumer<PistonLibInitializer> invoker) {
        FabricLoader.getInstance().invokeEntrypoints(MOD_ID, PistonLibInitializer.class, invoker);
    }

    @Override
    public ConfigManager createSimpleConfigManager(String modId, Class<?> configClass) {
        return new ConfigManagerImpl(modId, configClass);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void onStartServer(MinecraftServer s) {
        server = Optional.of(s);
        PistonLib.getConfigManager().initializeConfig();
        PistonLibBehaviorManager.load();
    }

    public static void onStopServer() {
        PistonLibBehaviorManager.save(false);
        server = Optional.empty();
    }
}

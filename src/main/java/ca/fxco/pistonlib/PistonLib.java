package ca.fxco.pistonlib;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

import ca.fxco.pistonlib.config.ConfigManager;
import ca.fxco.pistonlib.base.*;
import ca.fxco.pistonlib.config.ConfigManagerEntrypoint;
import ca.fxco.pistonlib.network.PLNetwork;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
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
    }

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
        ModItems.boostrap();
        ModCreativeModeTabs.bootstrap();
        ModRecipeTypes.boostrap();
        ModRecipeSerializers.boostrap();
        ModMenus.boostrap();
        ModScreens.boostrap();
    }

    private void initialize(Consumer<PistonLibInitializer> invoker) {
        EntrypointUtils.invoke(MOD_ID, PistonLibInitializer.class, invoker);
    }
}

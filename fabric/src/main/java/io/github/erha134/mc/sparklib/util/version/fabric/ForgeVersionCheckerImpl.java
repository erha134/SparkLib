package io.github.erha134.mc.sparklib.util.version.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;

import java.util.Optional;

public final class ForgeVersionCheckerImpl {
    private static Optional<String> getUpdateUrl(String modId) {
        return FabricLoader.getInstance()
                .getModContainer(modId)
                .map(ModContainer::getMetadata)
                .map(m -> m.getCustomValue("sparklib:updateUrl"))
                .map(CustomValue::getAsString);
    }
}

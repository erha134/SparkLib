package io.github.erha134.mc.sparklib;

import dev.architectury.platform.Platform;
import io.github.erha134.mc.sparklib.util.VersionChecker;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public final class SparkLib {
    public static final String MOD_ID = "sparklib";
    public static final String MOD_NAME = "Spark Lib";
    public static final String MOD_VERSION = Platform.getMod(MOD_ID).getVersion();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        if (Platform.isDevelopmentEnvironment()) {
            CompletableFuture.supplyAsync(() -> VersionChecker.doCheck("47.3.22",
                            "https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json"))
                    .thenAccept(c -> LOGGER.info("[Version Checker Test] status: {}, current: {}, recommended: {}, latest: {}",
                            c.getStatus(),
                            c.getCurrent(),
                            c.getRecommended(),
                            c.getLatest()));
        }
    }

    private SparkLib() {
        // NO-OP
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}

package com.example.test;

import com.example.test.registry.TestRegister;
import io.github.erha134.mc.sparklib.util.VersionChecker;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public final class TestMod {
    public static final String MOD_ID = "testmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        TestRegister.register();

        CompletableFuture.supplyAsync(() -> VersionChecker.doCheck("47.3.22",
                        "https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json"))
                .thenAccept(c -> LOGGER.info("[Version Checker Test] status: {}, current: {}, recommended: {}, latest: {}",
                        c.getStatus(),
                        c.getCurrent(),
                        c.getRecommended(),
                        c.getLatest()));
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}

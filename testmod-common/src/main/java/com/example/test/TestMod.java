package com.example.test;

import com.example.test.registry.TestRegister;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestMod {
    public static final String MOD_ID = "testmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        TestRegister.register();
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}

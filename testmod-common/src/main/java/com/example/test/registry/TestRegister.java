package com.example.test.registry;

import com.example.test.TestMod;
import io.github.erha134.mc.sparklib.registry.SRegistrar;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class TestRegister {
    public static final SRegistrar REGISTRAR = SRegistrar.getOrCreate(TestMod.MOD_ID);

    public static final Supplier<Registry<String>> TEST_REGISTRY;
    public static final Supplier<String> TEST;

    static {
        TEST_REGISTRY = REGISTRAR.createSimpleRegistry("test");
        TEST = REGISTRAR.registerSimple(TestRegister.TEST_REGISTRY.get(),
                "test",
                "This is a test text (1).");
    }

    public static void register() {
        REGISTRAR.register();
    }
}

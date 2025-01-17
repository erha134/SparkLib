package io.github.erha134.mc.sparklib.test;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.registry.RegisterUtils;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

public final class TestItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(SparkLib.MOD_ID, RegistryKeys.ITEM);

    public static final RegistrySupplier<Item> TEST_1 = ITEMS.register("test_1", RegisterUtils::createSimpleItem);

    public static void register() {
        ITEMS.register();
    }

    private TestItems() {
    }
}

package io.github.erha134.mc.sparklib.test;

import io.github.erha134.mc.sparklib.SparkLib;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class TestTags {
    public static final TagKey<Item> TEST_ITEMS = TagKey.of(RegistryKeys.ITEM, SparkLib.id("test_items"));

    public static void register() {
    }
}

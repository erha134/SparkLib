package io.github.erha134.mc.sparklib.data.provider;

import com.google.gson.JsonObject;
import io.github.erha134.easylib.string.StringFormatter;
import io.github.erha134.mc.sparklib.data.SDataGeneration;
import net.minecraft.block.Block;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.stat.StatType;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SLanguageProvider extends SDataProvider {
    private final String language;

    public SLanguageProvider(String modId, DataGenerator generator, String language) {
        super(StringFormatter.format("Language Provider by Spark Lib ({})", language), modId, generator);
        this.language = language;
    }

    public abstract void translate(Translator translator);

    @Override
    public void run(DataCache cache) throws IOException {
        Map<String, String> translations = new LinkedHashMap<>();
        this.translate((k, v) -> {
            if (translations.containsKey(k)) {
                throw new IllegalArgumentException("Duplicate translation key: " + k);
            }

            translations.put(k, v);
        });

        JsonObject jsonObject = new JsonObject();
        translations.forEach(jsonObject::addProperty);
        DataProvider.writeToPath(SDataGeneration.createGson(), cache, jsonObject, this.generator.getOutput()
                .resolve("assets")
                .resolve(this.modId)
                .resolve("lang")
                .resolve(this.language + ".json"));
    }

    @FunctionalInterface
    public interface Translator {
        void add(String key, String value);

        default void add(Item item, String value) {
            add(item.getTranslationKey(), value);
        }

        default void add(Block block, String value) {
            add(block.getTranslationKey(), value);
        }

        default void add(ItemGroup group, String value) {
            add("itemGroup." + group.getName(), value);
        }

        default void add(EntityType<?> entityType, String value) {
            add(entityType.getTranslationKey(), value);
        }

        default void add(Enchantment enchantment, String value) {
            add(enchantment.getTranslationKey(), value);
        }

        default void add(EntityAttribute entityAttribute, String value) {
            add(entityAttribute.getTranslationKey(), value);
        }

        default void add(StatType<?> statType, String value) {
            add(statType.getTranslationKey(), value);
        }

        default void add(StatusEffect statusEffect, String value) {
            add(statusEffect.getTranslationKey(), value);
        }
    }
}

package io.github.erha134.mc.sparklib.data.provider;

import com.google.gson.JsonObject;
import io.github.erha134.easylib.string.StringFormatter;
import io.github.erha134.mc.sparklib.data.SDataGeneration;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.stat.StatType;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class SLanguageProvider extends SDataProvider {
    private final String language;

    public SLanguageProvider(String modId, DataOutput output, String language) {
        super(StringFormatter.format("Language Provider by Spark Lib ({})", language), modId, output);
        this.language = language;
    }

    public abstract void translate(Translator translator);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<String, String> translations = new LinkedHashMap<>();
        this.translate((k, v) -> {
            if (translations.containsKey(k)) {
                throw new IllegalArgumentException("Duplicate translation key: " + k);
            }

            translations.put(k, v);
        });

        JsonObject jsonObject = new JsonObject();
        translations.forEach(jsonObject::addProperty);
        return DataProvider.writeToPath(writer,
                jsonObject,
                this.output.getResolver(DataOutput.OutputType.RESOURCE_PACK,
                        "lang")
                        .resolveJson(new Identifier(this.modId, this.language)));
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

        default void add(RegistryKey<ItemGroup> group, String value) {
            add(Registries.ITEM_GROUP.getOrThrow(group), value);
        }

        default void add(ItemGroup group, String value) {
            TextContent content = group.getDisplayName().getContent();

            if (content instanceof TranslatableTextContent tr) {
                add(tr.getKey(), value);
                return;
            }

            throw new IllegalArgumentException("The display name of item group is not translatable.");
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
            Identifier id = Registries.STAT_TYPE.getId(statType);

            if (id == null) {
                SDataGeneration.LOGGER.error("No identifier for stat type: {}", statType.getName().getString());
                return;
            }

            add(id.toString().replace(':', '.'), value);
        }

        default void add(StatusEffect statusEffect, String value) {
            add(statusEffect.getTranslationKey(), value);
        }

        default void add(Identifier identifier, String value) {
            add(identifier.toTranslationKey(), value);
        }
    }
}

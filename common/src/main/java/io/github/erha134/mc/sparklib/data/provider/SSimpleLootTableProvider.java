package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.erha134.easylib.string.StringFormatter;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class SSimpleLootTableProvider extends SRegistryDependentDataProvider implements LootTableGenerator, DataProvider {
    private final LootContextType lootContextType;

    public SSimpleLootTableProvider(String modId, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LootContextType lootContextType) {
        super(StringFormatter.format("{} Loot Table Provider by Spark Lib", LootContextTypes.MAP.inverse().get(lootContextType)),
                modId,
                output,
                registriesFuture);
        this.lootContextType = lootContextType;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<Identifier, LootTable> builders = Maps.newHashMap();
//        Map<Identifier, ConditionJsonProvider[]> conditionMap = new HashMap<>();

        return this.registriesFuture.thenCompose(lookup -> {
            accept(lookup, (registryKey, builder) -> {
                Identifier id = registryKey.getValue();

                if (builders.put(id, builder.type(this.lootContextType).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + id);
                }
            });

            RegistryOps<JsonElement> ops = lookup.getOps(JsonOps.INSTANCE);
            final List<CompletableFuture<?>> futures = new ArrayList<>();
            for (Map.Entry<Identifier, LootTable> entry : builders.entrySet()) {
                JsonObject tableJson = (JsonObject) LootTable.CODEC.encodeStart(ops, entry.getValue()).getOrThrow(IllegalStateException::new);
                futures.add(DataProvider.writeToPath(writer, tableJson, this.output
                        .getResolver(DataOutput.OutputType.DATA_PACK, "loot_tables")
                        .resolveJson(entry.getKey())));
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }
}

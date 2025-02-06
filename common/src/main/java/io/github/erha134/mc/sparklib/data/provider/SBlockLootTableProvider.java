package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.erha134.easylib.string.StringFormatter;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class SBlockLootTableProvider extends BlockLootTableGenerator implements DataProvider {
    private final String modId;
    private final DataOutput output;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;
    private final boolean validation;
    private final Set<Identifier> validationExcluded = new HashSet<>();

    public SBlockLootTableProvider(String modId, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, boolean validation) {
        super(Collections.emptySet(), FeatureFlags.FEATURE_MANAGER.getFeatureSet());
        this.modId = modId;
        this.output = output;
        this.registriesFuture = registriesFuture;
        this.validation = validation;
    }

    public SBlockLootTableProvider(String modId, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this(modId, output, registriesFuture, true);
    }

    @Override
    public abstract void generate();

    public void excludeFromValidation(Block block) {
        this.validationExcluded.add(Registries.BLOCK.getId(block));
    }

    @Override
    public void accept(RegistryWrapper.WrapperLookup registryLookup, BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        generate();

        for (Map.Entry<RegistryKey<LootTable>, LootTable.Builder> entry : lootTables.entrySet()) {
            RegistryKey<LootTable> id = entry.getKey();

            if (id.equals(LootTables.EMPTY)) {
                continue;
            }

            exporter.accept(id, entry.getValue());
        }

        // Optional Checks
        if (this.validation) {
            Set<Identifier> missing = Sets.newHashSet();

            for (Identifier blockId : Registries.BLOCK.getIds()) {
                if (blockId.getNamespace().equals(this.modId)) {
                    RegistryKey<LootTable> blockLootTableId = Registries.BLOCK.get(blockId).getLootTableKey();

                    if (blockLootTableId.getValue().getNamespace().equals(this.modId)) {
                        if (!lootTables.containsKey(blockLootTableId)) {
                            missing.add(blockId);
                        }
                    }
                }
            }

            missing.removeAll(this.validationExcluded);

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing loot table(s) for %s".formatted(missing));
            }
        }
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<Identifier, LootTable> builders = Maps.newHashMap();
//        Map<Identifier, ConditionJsonProvider[]> conditionMap = new HashMap<>();

        return this.registriesFuture.thenCompose(lookup -> {
            accept(lookup, (registryKey, builder) -> {
                Identifier id = registryKey.getValue();

                if (builders.put(id, builder.type(LootContextTypes.BLOCK).build()) != null) {
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

    @Override
    public String getName() {
        return StringFormatter.format("Block Loot Table Provider by Spark Lib ({})", this.modId);
    }
}

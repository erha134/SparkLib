package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public abstract class SBlockLootTableProvider extends BlockLootTableGenerator implements DataProvider {
    private final String modId;
    private final DataGenerator generator;
    private final boolean validation;
    private final Set<Identifier> validationExcluded = new HashSet<>();

    public SBlockLootTableProvider(String modId, DataGenerator generator, boolean validation) {
        this.modId = modId;
        this.generator = generator;
        this.validation = validation;
    }

    public SBlockLootTableProvider(String modId, DataGenerator generator) {
        this(modId, generator, true);
    }

    public abstract void generate();

    public void excludeFromValidation(Block block) {
        this.validationExcluded.add(Registry.BLOCK.getId(block));
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        generate();

        for (Map.Entry<Identifier, LootTable.Builder> entry : lootTables.entrySet()) {
            Identifier id = entry.getKey();

            if (id.equals(LootTables.EMPTY)) {
                continue;
            }

            exporter.accept(id, entry.getValue());
        }

        // Optional Checks
        if (this.validation) {
            Set<Identifier> missing = Sets.newHashSet();

            for (Identifier blockId : Registry.BLOCK.getIds()) {
                if (blockId.getNamespace().equals(this.modId)) {
                    Identifier blockLootTableId = Registry.BLOCK.get(blockId).getLootTableId();

                    if (blockLootTableId.getNamespace().equals(this.modId)) {
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
    public void run(DataWriter writer) throws IOException {
        Map<Identifier, LootTable> builders = Maps.newHashMap();
//        Map<Identifier, ConditionJsonProvider[]> conditionMap = new HashMap<>();

        accept((id, builder) -> {
//            ConditionJsonProvider[] conditions = FabricDataGenHelper.consumeConditions(builder);
//            conditionMap.put(id, conditions);

            if (builders.put(id, builder.type(LootContextTypes.BLOCK).build()) != null) {
                throw new IllegalStateException("Duplicate loot table " + id);
            }
        });

        for (Map.Entry<Identifier, LootTable> entry : builders.entrySet()) {
            JsonObject tableJson = (JsonObject) LootManager.toJson(entry.getValue());
//            ConditionJsonProvider.write(tableJson, conditionMap.remove(entry.getKey()));

            // getOutputPath(fabricDataOutput, entry.getKey())
            DataProvider.writeToPath(writer, tableJson, this.generator.getOutput()
                    .resolve("data")
                    .resolve(entry.getKey().getNamespace())
                    .resolve("loot_tables")
                    .resolve(entry.getKey().getPath() + ".json"));
        }
    }

    @Override
    public String getName() {
        return "Block Loot Table Provider by Spark Lib";
    }
}

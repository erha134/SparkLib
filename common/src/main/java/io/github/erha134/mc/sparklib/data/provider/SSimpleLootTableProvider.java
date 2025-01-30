package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import io.github.erha134.easylib.string.StringFormatter;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class SSimpleLootTableProvider extends SDataProvider implements Consumer<BiConsumer<Identifier, LootTable.Builder>>, DataProvider {
    private final LootContextType lootContextType;

    public SSimpleLootTableProvider(String modId, DataGenerator generator, LootContextType lootContextType) {
        super(StringFormatter.format("{} Loot Table Provider by Spark Lib", LootContextTypes.getId(lootContextType)),
                modId,
                generator);
        this.lootContextType = lootContextType;
    }

    @Override
    public void run(DataWriter writer) throws IOException {
        Map<Identifier, LootTable> builders = Maps.newHashMap();
//        Map<Identifier, ConditionJsonProvider[]> conditionMap = new HashMap<>();

        accept((id, builder) -> {
//            ConditionJsonProvider[] conditions = FabricDataGenHelper.consumeConditions(builder);
//            conditionMap.put(id, conditions);

            if (builders.put(id, builder.type(this.lootContextType).build()) != null) {
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
}

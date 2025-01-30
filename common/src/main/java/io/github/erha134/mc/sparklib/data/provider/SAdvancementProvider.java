package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import io.github.erha134.easylib.string.StringFormatter;
import net.minecraft.advancement.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;

public abstract class SAdvancementProvider extends SDataProvider {
    public SAdvancementProvider(String modId, DataGenerator generator) {
        super("Advancement Provider by Spark Lib", modId, generator);
    }

    public abstract void generate(Consumer<Advancement> consumer);

    @Override
    public void run(DataWriter writer) throws IOException {
        final Set<Identifier> ids = Sets.newHashSet();
        final Set<Advancement> advancements = Sets.newHashSet();

        generate(advancements::add);

        for (Advancement advancement : advancements) {
            if (!ids.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            }

            JsonObject advancementJson = advancement.createTask().toJson();
//            ConditionJsonProvider.write(advancementJson, FabricDataGenHelper.consumeConditions(advancement));

            DataProvider.writeToPath(writer, advancementJson,
                    this.generator.getOutput().resolve(StringFormatter.format("data/{}/advancements/{}.json",
                            advancement.getId().getNamespace(),
                            advancement.getId().getPath())));
        }
    }
}

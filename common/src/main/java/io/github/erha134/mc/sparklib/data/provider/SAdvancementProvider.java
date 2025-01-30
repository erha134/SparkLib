package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class SAdvancementProvider extends SDataProvider {
    public SAdvancementProvider(String modId, DataOutput output) {
        super("Advancement Provider by Spark Lib", modId, output);
    }

    public abstract void generate(Consumer<Advancement> consumer);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        final Set<Identifier> ids = Sets.newHashSet();
        final Set<Advancement> advancements = Sets.newHashSet();

        generate(advancements::add);

        final List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Advancement advancement : advancements) {
            if (!ids.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            }

            JsonObject advancementJson = advancement.createTask().toJson();
//            ConditionJsonProvider.write(advancementJson, FabricDataGenHelper.consumeConditions(advancement));

            futures.add(DataProvider.writeToPath(writer,
                    advancementJson,
                    this.output.getResolver(DataOutput.OutputType.DATA_PACK, "advancements")
                            .resolveJson(advancement.getId())));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }
}

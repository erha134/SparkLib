package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class SAdvancementProvider extends SDataProvider {
    public SAdvancementProvider(String modId, DataOutput output) {
        super("Advancement Provider by Spark Lib", modId, output);
    }

    public abstract void generate(Consumer<AdvancementEntry> consumer);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        final Set<Identifier> ids = Sets.newHashSet();
        final Set<AdvancementEntry> advancements = Sets.newHashSet();

        generate(advancements::add);

        final List<CompletableFuture<?>> futures = new ArrayList<>();

        for (AdvancementEntry advancement : advancements) {
            if (!ids.add(advancement.id())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.id());
            }

            JsonObject advancementJson = Util.getResult(Advancement.CODEC.encodeStart(JsonOps.INSTANCE, advancement.value()), IllegalStateException::new)
                    .getAsJsonObject();
//            ConditionJsonProvider.write(advancementJson, FabricDataGenHelper.consumeConditions(advancement));

            futures.add(DataProvider.writeToPath(writer,
                    advancementJson,
                    this.output.getResolver(DataOutput.OutputType.DATA_PACK, "advancements")
                            .resolveJson(advancement.id())));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }
}

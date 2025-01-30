package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import io.github.erha134.easylib.string.StringFormatter;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class SRecipeProvider extends RecipeProvider {
    private final String modId;

    public SRecipeProvider(String modId, DataOutput output) {
        super(output);
        this.modId = modId;
    }

    @Override
    public abstract void generate(Consumer<RecipeJsonProvider> exporter);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Set<Identifier> generatedRecipes = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        generate(provider -> {
            Identifier id = getRecipeIdentifier(provider.getRecipeId());

            if (!generatedRecipes.add(id)) {
                throw new IllegalStateException("Duplicate recipe " + id);
            }

            list.add(DataProvider.writeToPath(writer,
                    provider.toJson(),
                    this.recipesPathResolver.resolveJson(id)));

            JsonObject advancementJson = provider.toAdvancementJson();
            if (advancementJson != null) {
                list.add(DataProvider.writeToPath(writer,
                        advancementJson,
                        this.advancementsPathResolver.resolveJson(getRecipeIdentifier(provider.getAdvancementId()))));
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public final String getName() {
        return StringFormatter.format("Recipe Provider by Spark Lib ({})", this.modId);
    }

    protected Identifier getRecipeIdentifier(Identifier id) {
        return new Identifier(this.modId, id.getPath());
    }
}

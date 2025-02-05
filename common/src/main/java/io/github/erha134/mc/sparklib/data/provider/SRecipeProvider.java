package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public abstract class SRecipeProvider extends RecipeProvider {
    private final String modId;
    private final DataGenerator generator;

    public SRecipeProvider(String modId, DataGenerator generator) {
        super(generator);
        this.modId = modId;
        this.generator = generator;
    }

    public abstract void generate(Consumer<RecipeJsonProvider> exporter);

    @Override
    public void run(DataWriter writer)  {
        Set<Identifier> generatedRecipes = Sets.newHashSet();

        generate(provider -> {
            Identifier id = getRecipeIdentifier(provider.getRecipeId());

            if (!generatedRecipes.add(id)) {
                throw new IllegalStateException("Duplicate recipe " + id);
            }

            Path recipePath = this.generator.getOutput()
                    .resolve("data")
                    .resolve(id.getNamespace())
                    .resolve("recipes")
                    .resolve(id.getPath() + ".json");
            try {
                DataProvider.writeToPath(writer, provider.toJson(), recipePath);
            } catch (IOException e) {
                LOGGER.error("Couldn't save recipe {}", recipePath, e);
            }

            JsonObject advancementJson = provider.toAdvancementJson();
            if (advancementJson != null) {
                Identifier recipeAdvancementId = getRecipeIdentifier(provider.getAdvancementId());
                Path advancementPath = this.generator.getOutput()
                        .resolve("data")
                        .resolve(recipeAdvancementId.getNamespace())
                        .resolve("advancements")
                        .resolve(recipeAdvancementId.getPath() + ".json");
                try {
                    DataProvider.writeToPath(writer, advancementJson, advancementPath);
                } catch (IOException e) {
                    LOGGER.error("Couldn't save recipe advancement {}", advancementPath, e);
                }
            }
        });
    }

    @Override
    public String getName() {
        return "Recipe Provider by Spark Lib";
    }

    protected Identifier getRecipeIdentifier(Identifier id) {
        return new Identifier(this.modId, id.getPath());
    }
}

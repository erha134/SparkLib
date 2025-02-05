package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class SRecipeProvider extends RecipeProvider {
    private final String modId;

    public SRecipeProvider(String modId, DataOutput output) {
        super(output);
        this.modId = modId;
    }

    @Override
    public abstract void generate(RecipeExporter exporter);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Set<Identifier> generatedRecipes = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        generate(new RecipeExporter() {
            @Override
            public void accept(Identifier recipeId, Recipe<?> recipe, @Nullable AdvancementEntry advancement) {
                Identifier id = getRecipeIdentifier(recipeId);

                if (!generatedRecipes.add(id)) {
                    throw new IllegalStateException("Duplicate recipe " + id);
                }

                JsonObject recipeJson = Util.getResult(Recipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe), IllegalStateException::new)
                        .getAsJsonObject();

                list.add(DataProvider.writeToPath(writer, recipeJson,
                        SRecipeProvider.this.recipesPathResolver.resolveJson(id)));

                if (advancement != null) {
                    JsonObject advancementJson = Util.getResult(Advancement.CODEC.encodeStart(JsonOps.INSTANCE, advancement.value()),
                                    IllegalStateException::new)
                            .getAsJsonObject();

                    list.add(DataProvider.writeToPath(writer,
                            advancementJson,
                            SRecipeProvider.this.advancementsPathResolver.resolveJson(getRecipeIdentifier(advancement.id()))));
                }
            }

            @Override
            public Advancement.Builder getAdvancementBuilder() {
                return Advancement.Builder.createUntelemetered().parent(CraftingRecipeJsonBuilder.ROOT);
            }
        });

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Recipe Provider by Spark Lib";
    }

    protected Identifier getRecipeIdentifier(Identifier id) {
        return new Identifier(this.modId, id.getPath());
    }
}

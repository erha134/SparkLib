package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class SModelProvider extends ModelProvider {
    private final String modId;
    private final boolean validation;

    public SModelProvider(String modId, DataOutput output, boolean validation) {
        super(output);
        this.modId = modId;
        this.validation = validation;
    }

    public SModelProvider(String modId, DataOutput output) {
        this(modId, output, true);
    }

    public abstract void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator);

    public abstract void generateItemModels(ItemModelGenerator itemModelGenerator);

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<Block, BlockStateSupplier> blockMap = Maps.newHashMap();
        Map<Identifier, Supplier<JsonElement>> jsonMap = Maps.newHashMap();
        Set<Item> items = Sets.newHashSet();

        // Block State Model Generate
        BlockStateModelGenerator blockStateModelGenerator = new BlockStateModelGenerator(bsSupplier -> {
            Block block = bsSupplier.getBlock();

            if (this.validation) {
                // Skip over blocks and items that are not from the mod we are processing.
                if (!Registries.BLOCK.getId(block).getNamespace().equals(this.modId)) {
                    return;
                }
            }

            BlockStateSupplier blockStateSupplier2 = blockMap.put(block, bsSupplier);
            if (blockStateSupplier2 != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        }, (id, jsonSupplier) -> {
            Supplier<JsonElement> supplier = jsonMap.put(id, jsonSupplier);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        }, items::add);
        generateBlockStateModels(blockStateModelGenerator);
        blockStateModelGenerator.register();

        // Item Model Generate
        ItemModelGenerator itemModelGenerator = new ItemModelGenerator((id, jsonSupplier) -> {
            Supplier<JsonElement> supplier = jsonMap.put(id, jsonSupplier);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        });
        generateItemModels(itemModelGenerator);
        itemModelGenerator.register();

        // Check
        List<Block> blocks = Registries.BLOCK.stream()
                .filter(block -> !blockMap.containsKey(block))
                .toList();
        if (!blocks.isEmpty()) {
            throw new IllegalStateException("Missing blockstate definitions for: " + blocks);
        } else {
            Registries.BLOCK.forEach(block -> {
                Item item = Item.BLOCK_ITEMS.get(block);
                if (item != null) {
                    if (items.contains(item)) {
                        return;
                    }

                    // Only generate the item model if the block state json was registered
                    if (!blockMap.containsKey(block)) {
                        return;
                    }

                    if (!Registries.ITEM.getId(item).getNamespace().equals(this.modId)) {
                        // Skip over any items from other mods.
                        return;
                    }

                    Identifier id = ModelIds.getItemModelId(item);
                    if (!jsonMap.containsKey(id)) {
                        jsonMap.put(id, new SimpleModelSupplier(ModelIds.getBlockModelId(block)));
                    }
                }
            });

            CompletableFuture<?> writeBlockStateModelJsonFuture = CompletableFuture.allOf(blockMap.entrySet()
                    .stream()
                    .map(entry -> {
                        Path path = this.blockstatesPathResolver.resolveJson(entry.getKey().getRegistryEntry().registryKey().getValue());
                        return DataProvider.writeToPath(writer, entry.getValue().get(), path);
                    })
                    .toArray(CompletableFuture[]::new));

            CompletableFuture<?> writeItemModelJsonFuture = CompletableFuture.allOf(jsonMap.entrySet()
                    .stream()
                    .map(entry -> {
                        Path path = this.modelsPathResolver.resolveJson(entry.getKey());
                        return DataProvider.writeToPath(writer, entry.getValue().get(), path);
                    })
                    .toArray(CompletableFuture[]::new));

            return CompletableFuture.allOf(writeBlockStateModelJsonFuture, writeItemModelJsonFuture);
        }
    }

    @Override
    public String getName() {
        return "Model Provider by Spark Lib";
    }
}

package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import io.github.erha134.easylib.string.StringFormatter;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public abstract class SModelProvider extends ModelProvider {
    private final String modId;
    private final DataGenerator generator;
    private final boolean validation;

    public SModelProvider(String modId, DataGenerator generator, boolean validation) {
        super(generator);
        this.modId = modId;
        this.generator = generator;
        this.validation = validation;
    }

    public SModelProvider(String modId, DataGenerator generator) {
        this(modId, generator, true);
    }

    public abstract void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator);

    public abstract void generateItemModels(ItemModelGenerator itemModelGenerator);

    // TODO: Skip over blocks and items that are not from the mod we are processing.
    @Override
    public void run(DataWriter writer) {
        Map<Block, BlockStateSupplier> blockMap = Maps.newHashMap();
        Map<Identifier, Supplier<JsonElement>> jsonMap = Maps.newHashMap();
        Set<Item> items = Sets.newHashSet();

        // Block State Model Generate
        BlockStateModelGenerator blockStateModelGenerator = new BlockStateModelGenerator(bsSupplier -> {
            Block block = bsSupplier.getBlock();
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
        List<Block> blocks = Registry.BLOCK.stream()
                .filter(block -> !blockMap.containsKey(block))
                .toList();
        if (!blocks.isEmpty()) {
            throw new IllegalStateException("Missing blockstate definitions for: " + blocks);
        } else {
            Registry.BLOCK.forEach(block -> {
                Item item = Item.BLOCK_ITEMS.get(block);
                if (item != null) {
                    if (items.contains(item)) {
                        return;
                    }

                    Identifier id = ModelIds.getItemModelId(item);
                    if (!jsonMap.containsKey(id)) {
                        jsonMap.put(id, new SimpleModelSupplier(ModelIds.getBlockModelId(block)));
                    }
                }
            });


            jsonMap.forEach((key, value) -> {
                Path path = this.generator.getOutput()
                        .resolve("assets")
                        .resolve(key.getNamespace())
                        .resolve("models")
                        .resolve(key.getPath() + ".json");

                try {
                    DataProvider.writeToPath(writer, value.get(), path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            blockMap.forEach((key, value) -> {
                Identifier id = key.sparklib$entryId();

                Path path = this.generator.getOutput()
                        .resolve("assets")
                        .resolve(id.getNamespace())
                        .resolve("blockstates")
                        .resolve(id.getPath() + ".json");

                try {
                    DataProvider.writeToPath(writer, value.get(), path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public String getName() {
        return "Model Provider by Spark Lib";
    }
}

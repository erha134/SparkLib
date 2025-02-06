package io.github.erha134.mc.sparklib.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.erha134.mc.sparklib.data.factory.SDataProviderFactory;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class SDataGeneration {
    private final String modId;
    private final DataGenerator generator;
    private final List<SDataProviderFactory<?>> providerFactories = new ArrayList<>();

    public SDataGeneration(String modId,
                           DataGenerator generator) {
        this.modId = modId;
        this.generator = generator;
    }

    public <T extends DataProvider> void addProvider(SDataProviderFactory<T> factory) {
        this.providerFactories.add(factory);
    }

    public void run() {
        this.providerFactories.forEach(f -> {
            this.generator.addProvider(f.create(this.modId, this.generator));
        });
    }

    public static Gson createGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}

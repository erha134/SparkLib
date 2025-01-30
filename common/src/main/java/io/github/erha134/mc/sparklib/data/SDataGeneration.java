package io.github.erha134.mc.sparklib.data;

import io.github.erha134.mc.sparklib.data.factory.SDataProviderFactory;
import io.github.erha134.mc.sparklib.data.factory.SDataRegistryDependentProviderFactory;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SDataGeneration {
    private final String modId;
    private final DataGenerator.Pack pack;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;
    private final List<SDataProviderFactory<?>> providerFactories = new ArrayList<>();
    private final List<SDataRegistryDependentProviderFactory<?>> registryDependentProviderFactories = new ArrayList<>();

    private SDataGeneration(String modId,
                            DataGenerator.Pack pack,
                            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.modId = modId;
        this.pack = pack;
        this.registriesFuture = registriesFuture;
    }

    public SDataGeneration(String modId,
                           DataGenerator generator,
                           CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this(modId, generator.createVanillaSubPack(true, modId), registriesFuture);
    }

    public <T extends DataProvider> void addProvider(SDataProviderFactory<T> factory) {
        this.providerFactories.add(factory);
    }

    public <T extends DataProvider> void addProvider(SDataRegistryDependentProviderFactory<T> factory) {
        this.registryDependentProviderFactories.add(factory);
    }

    public void run() {
        this.providerFactories.forEach(f -> {
            this.pack.addProvider(output -> f.create(this.modId, output));
        });

        this.registryDependentProviderFactories.forEach(f -> {
            this.pack.addProvider(output -> f.create(this.modId, output, this.registriesFuture));
        });
    }
}

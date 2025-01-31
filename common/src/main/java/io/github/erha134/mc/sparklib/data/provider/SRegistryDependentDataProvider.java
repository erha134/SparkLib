package io.github.erha134.mc.sparklib.data.provider;

import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public abstract class SRegistryDependentDataProvider extends SDataProvider {
    protected final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    public SRegistryDependentDataProvider(String name,
                                          String modId,
                                          DataOutput output,
                                          CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(name, modId, output);
        this.registriesFuture = registriesFuture;
    }
}

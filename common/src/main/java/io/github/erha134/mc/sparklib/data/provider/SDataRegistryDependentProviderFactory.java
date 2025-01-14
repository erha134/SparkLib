package io.github.erha134.mc.sparklib.data.provider;

import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface SDataRegistryDependentProviderFactory<T extends DataProvider> {
    T create(String modId, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture);
}

package io.github.erha134.mc.sparklib.registry.v0.api;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.function.Supplier;

public class WrappedDeferredRegister<T> {
    protected final DeferredRegister<T> delegate;

    protected WrappedDeferredRegister(String modId, Registry<T> registry) {
        this(modId, (RegistryKey<Registry<T>>) registry.getKey());
    }

    public WrappedDeferredRegister(String modId, RegistryKey<Registry<T>> registryKey) {
        this.delegate = DeferredRegister.create(modId, registryKey);
    }

    public <R extends T> WrappedRegistrySupplier<R> register(String id, Supplier<R> supplier) {
        return new WrappedRegistrySupplier<>(this.delegate.register(id, supplier));
    }

    public void register() {
        this.delegate.register();
    }
}

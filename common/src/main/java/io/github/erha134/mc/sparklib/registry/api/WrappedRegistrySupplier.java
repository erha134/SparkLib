package io.github.erha134.mc.sparklib.registry.api;

import dev.architectury.registry.registries.RegistrySupplier;

import java.util.function.Supplier;

public class WrappedRegistrySupplier<T> implements Supplier<T> {
    protected final RegistrySupplier<T> delegate;

    public WrappedRegistrySupplier(RegistrySupplier<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        return this.delegate.get();
    }
}

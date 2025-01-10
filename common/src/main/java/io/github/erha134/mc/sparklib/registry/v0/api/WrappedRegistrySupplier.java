package io.github.erha134.mc.sparklib.registry.v0.api;

import dev.architectury.registry.registries.RegistrySupplier;

public class WrappedRegistrySupplier<T> {
    protected final RegistrySupplier<T> delegate;

    public WrappedRegistrySupplier(RegistrySupplier<T> delegate) {
        this.delegate = delegate;
    }
}

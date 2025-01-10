package io.github.erha134.mc.sparklib.registry.v0.api;

import dev.architectury.registry.registries.RegistrySupplier;

public class DoubleWrappedRegisterSupplier<T1, T2> {
    protected final RegistrySupplier<T1> delegate1;
    protected final RegistrySupplier<T2> delegate2;

    public DoubleWrappedRegisterSupplier(RegistrySupplier<T1> delegate1, RegistrySupplier<T2> delegate2) {
        this.delegate1 = delegate1;
        this.delegate2 = delegate2;
    }
}

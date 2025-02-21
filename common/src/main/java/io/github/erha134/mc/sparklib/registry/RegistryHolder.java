package io.github.erha134.mc.sparklib.registry;

import io.github.erha134.easylib.function.supplier.OptionalSupplier;
import lombok.Getter;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class RegistryHolder<T> implements OptionalSupplier<T> {
    private final Supplier<T> delegate;
    @Getter
    private final Identifier id;
    @Getter
    private final RegistryKey<? super T> registryKey;

    public RegistryHolder(Supplier<T> delegate, Identifier id, RegistryKey<? super T> registryKey) {
        this.delegate = delegate;
        this.id = id;
//        this.registryKey = RegistryKey.of(registryKey, id);
        this.registryKey = registryKey;
    }

    @Override
    public T get() {
        return this.delegate.get();
    }
}

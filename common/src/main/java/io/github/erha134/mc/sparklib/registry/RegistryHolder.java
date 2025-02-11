package io.github.erha134.mc.sparklib.registry;

import lombok.Getter;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class RegistryHolder<T> implements Supplier<T> {
    private final Supplier<T> delegate;
    @Getter
    private final Identifier id;

    public RegistryHolder(Supplier<T> delegate, Identifier id) {
        this.delegate = delegate;
        this.id = id;
    }

    @Override
    public T get() {
        return this.delegate.get();
    }
}

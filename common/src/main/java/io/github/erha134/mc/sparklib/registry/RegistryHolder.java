package io.github.erha134.mc.sparklib.registry;

import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class RegistryHolder<T> implements Supplier<T> {
    private final Identifier id;
    private final T entry;

    public RegistryHolder(Identifier id, T entry) {
        this.id = id;
        this.entry = entry;
    }

    public RegistryHolder(Identifier id, Supplier<T> entry) {
        this(id, entry.get());
    }

    public Identifier id() {
        return this.id;
    }

    @Override
    public T get() {
        return this.entry;
    }

    public boolean isPresent() {
        return this.entry != null;
    }
}

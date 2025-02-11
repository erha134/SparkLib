package io.github.erha134.mc.sparklib.registry.arch;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class DeferredRegisterManager {
    private final String modId;
    private final Map<Registry<?>, DeferredRegister<?>> deferredRegisters = new LinkedHashMap<>();

    public DeferredRegisterManager(String modId) {
        this.modId = modId;
    }

    public <R, T extends R> RegistrySupplier<R> register(Registry<T> registry, String id, Supplier<R> supplier) {
        return ((DeferredRegister<R>) this.deferredRegisters.computeIfAbsent(registry,
                $ -> DeferredRegister.create(this.modId, (RegistryKey<Registry<T>>) registry.getKey())))
                .register(id, supplier);
    }

    public <R, T extends R> RegistrySupplier<R> registerSimple(Registry<T> registry, String id, T value) {
        return this.register(registry, id, () -> value);
    }

    public <T> TagKey<T> tag(Registry<T> registry, String id) {
        return TagKey.of(registry.getKey(), new Identifier(this.modId, id));
    }

    public final void register() {
        this.deferredRegisters.values().forEach(DeferredRegister::register);
    }
}

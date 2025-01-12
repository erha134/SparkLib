package io.github.erha134.mc.sparklib.registry.api;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public interface Registrable<T> {
    Registry<T> sparklib$registry();

    RegistryEntry<T> sparklib$registryEntry();

    default RegistryKey<T> sparklib$registryKey() {
        return sparklib$registryEntry().getKey().orElseThrow();
    }

    default Identifier sparklib$id() {
        return sparklib$registryKey().getValue();
    }
}

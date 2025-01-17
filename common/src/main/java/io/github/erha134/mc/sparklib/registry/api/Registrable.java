package io.github.erha134.mc.sparklib.registry.api;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public interface Registrable<T> {
    Registry<T> sparklib$registry();

    default RegistryKey<Registry<T>> sparklib$registryKey() {
        return (RegistryKey<Registry<T>>) sparklib$registry().getKey();
    }

    RegistryEntry.Reference<T> sparklib$entry();

    default RegistryKey<T> sparklib$entryKey() {
        return sparklib$entry().registryKey();
    }

    default Identifier sparklib$entryId() {
        return sparklib$entryKey().getValue();
    }
}

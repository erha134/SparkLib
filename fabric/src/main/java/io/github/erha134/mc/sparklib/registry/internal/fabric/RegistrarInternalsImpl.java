package io.github.erha134.mc.sparklib.registry.internal.fabric;

import io.github.erha134.mc.sparklib.registry.RegistryHolder;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

@ApiStatus.Internal
public final class RegistrarInternalsImpl {
    public static <R, T extends R> void register(String modId, Registry<R> registry, Collection<RegistryHolder<T>> holders) {
        holders.forEach(h -> {
            Registry.register(registry, h.id(), h.get());
        });
    }
}

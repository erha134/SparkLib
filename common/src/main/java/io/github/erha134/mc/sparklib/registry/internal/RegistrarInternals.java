package io.github.erha134.mc.sparklib.registry.internal;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.erha134.mc.sparklib.registry.RegistryHolder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class RegistrarInternals {
    public static void forEachRegistries(Consumer<Registry<?>> consumer) {
        consumer.accept(Registries.FLUID);
        consumer.accept(Registries.BLOCK);
        consumer.accept(Registries.ITEM);

        Registries.REGISTRIES.forEach(r -> {
            if (r != Registries.FLUID && r != Registries.BLOCK && r != Registries.ITEM) { // ...
                consumer.accept(r);
            }
        });
    }

    @ExpectPlatform
    public static <R, T extends R> void register(String modId, Registry<R> registry, Collection<RegistryHolder<T>> holders) {
        throw new AssertionError();
    }
}

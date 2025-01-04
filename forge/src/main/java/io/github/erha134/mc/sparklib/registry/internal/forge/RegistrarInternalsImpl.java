package io.github.erha134.mc.sparklib.registry.internal.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.erha134.mc.sparklib.registry.RegistryHolder;
import net.minecraft.registry.Registry;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

@ApiStatus.Internal
public final class RegistrarInternalsImpl {
    public static <R, T extends R> void register(String modId, Registry<R> registry, Collection<RegistryHolder<T>> holders) {
        EventBuses.getModEventBus(modId)
                .orElseThrow()
                .addListener(EventPriority.HIGHEST, e -> {
                    if (e instanceof RegisterEvent re) {
                        re.register(registry.getKey(), helper -> {
                            holders.forEach(h -> {
                                helper.register(h.id(), h.get());
                            });
                        });
                    }
                });
    }
}

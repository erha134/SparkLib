package io.github.erha134.mc.sparklib.registry.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import dev.architectury.platform.forge.EventBuses;
import io.github.erha134.mc.sparklib.registry.RegistryHolder;
import io.github.erha134.mc.sparklib.registry.SRegistrar;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class SRegistrarImpl extends SRegistrar {
    private final IEventBus bus;
    private final Map<Identifier, DeferredRegister<?>> deferredRegisters = new HashMap<>();
    private final List<Consumer<DataPackRegistryEvent.NewRegistry>> newDatapackRegistries = new ArrayList<>();

    private SRegistrarImpl(String modId) {
        super(modId);
        this.bus = EventBuses.getModEventBus(modId).orElseThrow();
    }

    public static SRegistrar create(String modId) {
        return new SRegistrarImpl(modId);
    }

    @Override
    public <R, T extends R> RegistryHolder<T> register(Registry<R> registry, String id, Supplier<T> supplier) {
        RegistryKey<? extends Registry<R>> key = registry.getKey();
        ForgeRegistry<R> forgeRegistry = RegistryManager.ACTIVE.getRegistry(key);

        if (forgeRegistry != null) {
            DeferredRegister<R> deferredRegister = (DeferredRegister<R>) this.deferredRegisters.computeIfAbsent(
                    forgeRegistry.getRegistryKey().getValue(), $ -> DeferredRegister.create(forgeRegistry, this.modId));

            RegistryObject<T> object = deferredRegister.register(id, supplier);
            return new RegistryHolder<>(object, this.id(id), object.getKey());
        }

        DeferredRegister<R> deferredRegister = (DeferredRegister<R>) this.deferredRegisters.computeIfAbsent(
                key.getValue(), $ -> DeferredRegister.create(key, this.modId));

        RegistryObject<T> object = deferredRegister.register(id, supplier);
        return new RegistryHolder<>(object, this.id(id), object.getKey());
    }

    @Override
    protected ItemGroup.Builder createGroupBuilder(Text title, Supplier<ItemStack> icon) {
        return ItemGroup.builder().displayName(title).icon(icon);
    }

    // refer to https://github.com/CorgiTaco/CorgiLib/blob/1.20.X/Forge/src/main/java/corgitaco/corgilib/forge/platform/ForgePlatform.java
    // under the LGPL-v3 License
    @Override
    public <R> Supplier<Registry<R>> createSimpleRegistry(String id) {
        if (Registries.REGISTRIES instanceof SimpleRegistry<? extends Registry<?>> root) {
            root.unfreeze();
        }

        Registry<R> registry = Registries.create(this.registryKey(id), Lifecycle.stable(), builder -> (R) new Object());

        if (Registries.REGISTRIES instanceof SimpleRegistry<? extends Registry<?>> root) {
            root.freeze();
        }

        return () -> registry;
    }

    @Override
    public <R> Supplier<DefaultedRegistry<R>> createDefaultedRegistry(String id, String defaultValueId) {
        if (Registries.REGISTRIES instanceof SimpleRegistry<? extends Registry<?>> root) {
            root.unfreeze();
        }

        DefaultedRegistry<R> registry = Registries.create(this.registryKey(id),
                this.id(defaultValueId).toString(),
                Lifecycle.stable(),
                builder -> (R) new Object());

        if (Registries.REGISTRIES instanceof SimpleRegistry<? extends Registry<?>> root) {
            root.freeze();
        }

        return () -> registry;
    }

    @Override
    public <R> void createDatapackRegistry(String id, Codec<R> codec) {
        this.newDatapackRegistries.add(e -> e.dataPackRegistry(this.registryKey(id), codec));
    }

    @Override
    protected void onRegister() {
        this.deferredRegisters.values().forEach(r -> r.register(this.bus));
        this.bus.<DataPackRegistryEvent.NewRegistry>addListener(e ->
                this.newDatapackRegistries.forEach(c -> c.accept(e)));
    }
}

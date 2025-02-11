package io.github.erha134.mc.sparklib.registry.fabric;

import com.mojang.serialization.Codec;
import io.github.erha134.mc.sparklib.registry.RegistryHolder;
import io.github.erha134.mc.sparklib.registry.SRegistrar;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public final class SRegistrarImpl extends SRegistrar {
    private SRegistrarImpl(String modId) {
        super(modId);
    }

    public static SRegistrar create(String modId) {
        return new SRegistrarImpl(modId);
    }

    @Override
    public <R, T extends R> RegistryHolder<T> register(Registry<R> registry, String id, Supplier<T> supplier) {
        Identifier entryId = this.id(id);
        T entry = Registry.register(registry, entryId, supplier.get());
        return new RegistryHolder<>(() -> entry, entryId);
    }

    @Override
    protected ItemGroup.Builder createGroupBuilder(Text title, Supplier<ItemStack> icon) {
        return FabricItemGroup.builder().displayName(title).icon(icon);
    }

    @Override
    public <R> Supplier<Registry<R>> createSimpleRegistry(String id) {
        MutableRegistry<R> registry = FabricRegistryBuilder.createSimple(this.<R>registryKey(id)).buildAndRegister();
        return () -> registry;
    }

    @Override
    public <R> Supplier<DefaultedRegistry<R>> createDefaultedRegistry(String id, String defaultValueId) {
        DefaultedRegistry<R> registry = FabricRegistryBuilder.createDefaulted(this.<R>registryKey(id), this.id(defaultValueId))
                .buildAndRegister();
        return () -> registry;
    }

    @Override
    public <R> void createDatapackRegistry(String id, Codec<R> codec) {
        DynamicRegistries.registerSynced(this.registryKey(id), codec);
    }

    @Override
    protected void onRegister() {
    }
}

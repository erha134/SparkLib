package io.github.erha134.mc.sparklib.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.registry.internal.RegistrarInternals;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
public class Registrar {
    private final String modId;
    private boolean freeze = false;
    private final ListMultimap<Registry<?>, RegistryHolder<?>> entries = ArrayListMultimap.create();

    public Registrar(String modId) {
        this.modId = modId;
    }

    /**
     * Remember to call this method to finish registration.
     */
    public void register() {
        if (this.freeze) {
            throw new IllegalStateException("Registrar is already frozen");
        }

        this.freeze = true;
        RegistrarInternals.forEachRegistries(this::register);
    }

    private <R, T extends R> void register(Registry<R> registry) {
        ArrayList<RegistryHolder<T>> list = this.entries.get(registry)
                .stream()
                .map(h -> (RegistryHolder<T>) h)
                .collect(Collectors.toCollection(ArrayList::new));

        if (list.isEmpty()) {
            return;
        }

        list.forEach(h -> System.err.println(h.id().toString()));
        RegistrarInternals.register(this.modId, registry, list);
    }

    public <R, T extends R> RegistryHolder<T> register(Registry<R> registry, String id, Supplier<T> supplier) {
        RegistryHolder<T> holder = new RegistryHolder<>(this.createId(id), supplier);
        this.entries.put(registry, holder);
        return holder;
    }

    @ApiStatus.Experimental
    public <R, T extends R> RegistryHolder<T> register(Registry<R> registry, String id, T entry) {
        return this.register(registry, id, (Supplier<T>) () -> entry);
    }

    public <T extends Item> RegistryHolder<T> item(String id, Supplier<T> supplier) {
        return this.register(Registries.ITEM, id, supplier);
    }

    @ApiStatus.Experimental
    public <T extends Item> RegistryHolder<T> item(String id, T entry) {
        return this.item(id, (Supplier<T>) () -> entry);
    }

    public <T extends Block> RegistryHolder<T> block(String id, Supplier<T> value) {
        return this.register(Registries.BLOCK, id, value);
    }

    @ApiStatus.Experimental
    public <T extends Block> RegistryHolder<T> block(String id, T entry) {
        return this.block(id, (Supplier<T>) () -> entry);
    }

    private Identifier createId(String id) {
        return new Identifier(this.modId, id);
    }
}

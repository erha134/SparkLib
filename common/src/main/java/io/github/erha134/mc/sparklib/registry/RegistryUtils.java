package io.github.erha134.mc.sparklib.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public final class RegistryUtils {
    public static <T> void forEachValues(Registry<T> registry, Consumer<T> consumer) {
        registry.stream()
                .forEach(consumer);
    }

    public static <T> void forEachEntries(Registry<T> registry, Consumer<RegistryEntry<T>> consumer) {
        registry.streamEntries()
//                .filter(RegistryEntry.Reference::hasKeyAndValue)  // removes null entries
                .forEach(consumer);
    }

    public static <T> void forEachTagKeys(Registry<T> registry, Consumer<TagKey<T>> consumer) {
        registry.streamTags()
                .forEach(consumer);
    }

    public static <T> void forEachTagKeyEntries(Registry<T> registry, TagKey<T> tagKey, Consumer<T> consumer) {
        StreamSupport.stream(registry.iterateEntries(tagKey).spliterator(), false)
//                .filter(r -> r instanceof RegistryEntry.Reference<T>)
//                .filter(RegistryEntry::hasKeyAndValue)  // removes null entries
                .map(RegistryEntry::value)
                .forEach(consumer);
    }

    public static void forEachRegistries(Consumer<Registry<?>> consumer) {
        Registries.REGISTRIES.forEach(consumer);
    }

    public static <R> RegistryKey<R> getRegistryKeyForEntry(Registry<R> registry, R entry) {
        return registry.getKey(entry).orElseThrow();
    }
}

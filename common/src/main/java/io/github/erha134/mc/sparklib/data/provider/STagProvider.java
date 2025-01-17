package io.github.erha134.mc.sparklib.data.provider;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.erha134.easylib.string.StringFormatter;
import io.github.erha134.mc.sparklib.registry.api.Registrable;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class STagProvider<T> extends TagProvider<T> {
    private final String modId;
    private final DataOutput output;

    public STagProvider(String modId,
                        DataOutput output,
                        RegistryKey<? extends Registry<T>> registryRef,
                        CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        super(output, registryRef, registryLookupFuture);
        this.modId = modId;
        this.output = output;
    }

    @Override
    public abstract void configure(RegistryWrapper.WrapperLookup lookup);

    public RegistryKey<T> getEntryKey(T entry) {
        // FIXME
        Registry<T> registry = (Registry<T>) Registries.REGISTRIES.get(this.registryRef.getValue());
        if (registry != null) {
            Optional<RegistryKey<T>> key = registry.getKey(entry);

            if (key.isPresent()) {
                return key.get();
            }
        }

        throw new UnsupportedOperationException("Adding objects is not supported by " + getClass());
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        record RegistryInfo<T>(RegistryWrapper.WrapperLookup contents, TagProvider.TagLookup<T> parent) {
        }

        return this.getRegistryLookupFuture()
                .thenApply(registryLookupFuture -> {
                    this.registryLoadFuture.complete(null);
                    return registryLookupFuture;
                })
                .thenCombineAsync(this.parentTagLookupFuture, RegistryInfo::new)
                .thenCompose(info -> {
                    RegistryWrapper.Impl<T> impl = info.contents.getWrapperOrThrow(this.registryRef);
                    Predicate<Identifier> predicate = id -> impl.getOptional(RegistryKey.of(this.registryRef, id)).isPresent();
                    Predicate<Identifier> predicate2 = id -> this.tagBuilders.containsKey(id) || info.parent.contains(TagKey.of(this.registryRef, id));
                    return CompletableFuture.allOf(this.tagBuilders.entrySet()
                                    .stream()
                                    .map(entry -> {
                                        Identifier identifier = entry.getKey();
                                        TagBuilder tagBuilder = entry.getValue();
                                        List<TagEntry> list = tagBuilder.build();
                                        List<TagEntry> list2 = list.stream().filter(tagEntry -> !tagEntry.canAdd(predicate, predicate2)).toList();
                                        if (!list2.isEmpty()) {
                                            throw new IllegalArgumentException(
                                                    String.format(
                                                            Locale.ROOT,
                                                            "Couldn't define tag %s as it is missing following references: %s",
                                                            identifier,
                                                            list2.stream().map(Objects::toString).collect(Collectors.joining(","))
                                                    )
                                            );
                                        } else {
                                            JsonElement jsonElement = TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(list, false)).getOrThrow(false, LOGGER::error);
                                            Path path = this.output.getResolver(DataOutput.OutputType.DATA_PACK,
                                                            StringFormatter.format("{}/{}", this.modId, TagManagerLoader.getPath(this.registryRef)))
                                                    .resolveJson(identifier);
                                            return DataProvider.writeToPath(writer, jsonElement, path);
                                        }
                                    })
                                    .toArray(CompletableFuture[]::new)
                            );
                        }
                );
    }

    /**
     * 请使用 {@link #getOrCreateCustomTagBuilder(TagKey)}。
     * @deprecated
     */
    @Deprecated
    @Override
    protected ProvidedTagBuilder<T> getOrCreateTagBuilder(TagKey<T> tag) {
        throw new UnsupportedOperationException("because Forge");
    }

    protected SProvidedTagBuilder getOrCreateCustomTagBuilder(TagKey<T> tag) {
        return new SProvidedTagBuilder(super.getOrCreateTagBuilder(tag));
    }

    protected final class SProvidedTagBuilder {
        private final TagProvider.ProvidedTagBuilder<T> parent;
        private final TagBuilder builder;

        private SProvidedTagBuilder(ProvidedTagBuilder<T> parent) {
            this.parent = parent;
            this.builder = parent.builder;
        }

        public SProvidedTagBuilder add(T element) {
            this.add(STagProvider.this.getEntryKey(element));
            return this;
        }

        public SProvidedTagBuilder add(Supplier<T> supplier) {
            this.add(supplier.get());
            return this;
        }

        @SafeVarargs
        public final SProvidedTagBuilder add(T... element) {
            Stream.of(element)
                    .map(STagProvider.this::getEntryKey)
                    .forEach(this::add);
            return this;
        }

        @SafeVarargs
        public final SProvidedTagBuilder add(Supplier<T>... suppliers) {
            Stream.of(suppliers)
                    .map(Supplier::get)
                    .map(STagProvider.this::getEntryKey)
                    .forEach(this::add);
            return this;
        }

        public SProvidedTagBuilder add(Registrable<T> registrable) {
            this.add(registrable.sparklib$entryKey());
            return this;
        }

        public SProvidedTagBuilder add(RegistryKey<T> key) {
            this.builder.add(key.getValue());
            return this;
        }

        public SProvidedTagBuilder add(Identifier id) {
            builder.add(id);
            return this;
        }

        public SProvidedTagBuilder addOptional(Identifier id) {
            this.builder.addOptional(id);
            return this;
        }

        public SProvidedTagBuilder addOptional(RegistryKey<? extends T> registryKey) {
            return addOptional(registryKey.getValue());
        }

        public SProvidedTagBuilder addTag(TagKey<T> identifiedTag) {
            this.builder.addTag(identifiedTag.id());
            return this;
        }

        public SProvidedTagBuilder addOptionalTag(Identifier id) {
            this.parent.addOptionalTag(id);
            return this;
        }

        public SProvidedTagBuilder addOptionalTag(TagKey<T> tag) {
            return addOptionalTag(tag.id());
        }

        public SProvidedTagBuilder forceAddTag(TagKey<T> tag) {
            builder.add(new ForcedTagEntry(TagEntry.create(tag.id())));
            return this;
        }

        public SProvidedTagBuilder add(Identifier... ids) {
            for (Identifier id : ids) {
                add(id);
            }

            return this;
        }

        @SafeVarargs
        public final SProvidedTagBuilder add(RegistryKey<T>... registryKeys) {
            for (RegistryKey<T> registryKey : registryKeys) {
                add(registryKey);
            }

            return this;
        }
    }

    public static class ForcedTagEntry extends TagEntry {
        private final TagEntry delegate;

        public ForcedTagEntry(TagEntry delegate) {
            super(delegate.id, true, delegate.required);
            this.delegate = delegate;
        }

        @Override
        public <T> boolean resolve(ValueGetter<T> valueGetter, Consumer<T> idConsumer) {
            return this.delegate.resolve(valueGetter, idConsumer);
        }

        @Override
        public boolean canAdd(Predicate<Identifier> objectExistsTest, Predicate<Identifier> tagExistsTest) {
            return true;
        }
    }

    // Impls

    public static abstract class SItemTagProvider extends STagProvider<Item> {
        public SItemTagProvider(String modId,
                                DataOutput output,
                                CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
            super(modId, output, RegistryKeys.ITEM, registryLookupFuture);
        }

        @Override
        public RegistryKey<Item> getEntryKey(Item entry) {
            return entry.sparklib$entryKey();
        }
    }

    public static abstract class SBlockTagProvider extends STagProvider<Block> {
        public SBlockTagProvider(String modId,
                                 DataOutput output,
                                 CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
            super(modId, output, RegistryKeys.BLOCK, registryLookupFuture);
        }

        @Override
        public RegistryKey<Block> getEntryKey(Block entry) {
            return entry.sparklib$entryKey();
        }
    }
}

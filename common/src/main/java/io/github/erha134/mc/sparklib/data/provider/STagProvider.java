package io.github.erha134.mc.sparklib.data.provider;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.erha134.easylib.string.StringFormatter;
import io.github.erha134.mc.sparklib.registry.api.Registrable;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.erha134.mc.sparklib.data.SDataGeneration.LOGGER;

public abstract class STagProvider<T> extends TagProvider<T> {
    private final String modId;
    private final DataOutput output;
    private final Map<Identifier, STagBuilder> tagBuilders = new LinkedHashMap<>();

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

                    return CompletableFuture.allOf(this.tagBuilders.entrySet()
                                    .stream()
                                    .map(entry -> {
                                        Identifier identifier = entry.getKey();
                                        STagBuilder builder = entry.getValue();
                                        List<TagEntry> entries = builder.build()
                                                .stream()
                                                .filter(tagEntry -> !tagEntry.canAdd(id ->
                                                                impl.getOptional(RegistryKey.of(this.registryRef, id)).isPresent(),
                                                        id ->
                                                                this.tagBuilders.containsKey(id) ||
                                                                        info.parent.contains(TagKey.of(this.registryRef, id))))
                                                .toList();

                                        if (!entries.isEmpty()) {
                                            throw new IllegalArgumentException(
                                                    String.format(
                                                            Locale.ROOT,
                                                            "Couldn't define tag %s as it is missing following references: %s",
                                                            identifier,
                                                            entries.stream()
                                                                    .map(Objects::toString)
                                                                    .collect(Collectors.joining(","))
                                                    )
                                            );
                                        } else {
                                            JsonElement jsonElement = TagFile.CODEC.encodeStart(JsonOps.INSTANCE,
                                                    new TagFile(entries, builder.replace)).getOrThrow(false, LOGGER::error);
                                            Path path = this.output.getResolver(DataOutput.OutputType.DATA_PACK,
                                                            StringFormatter.format("{}/{}",
                                                                    this.modId,
                                                                    TagManagerLoader.getPath(this.registryRef)))
                                                    .resolveJson(identifier);
                                            return DataProvider.writeToPath(writer, jsonElement, path);
                                        }
                                    })
                                    .toArray(CompletableFuture[]::new)
                            );
                        }
                );
    }

    @Override
    public String getName() {
        return StringFormatter.format("Tag Provider by Spark Lib ({})", this.registryRef.getValue());
    }

    /**
     * 请使用 {@link #getOrCreateSTagBuilder(TagKey)} 和 {@link #getOrCreateSTagBuilder(TagKey, boolean)}。
     * @deprecated
     */
    @Deprecated
    @Override
    protected ProvidedTagBuilder<T> getOrCreateTagBuilder(TagKey<T> tag) {
        throw new UnsupportedOperationException("because Forge");
    }

    protected STagBuilder getOrCreateSTagBuilder(TagKey<T> tag) {
        return this.getOrCreateSTagBuilder(tag, false);
    }

    protected STagBuilder getOrCreateSTagBuilder(TagKey<T> tag, boolean replace) {
        return this.tagBuilders.computeIfAbsent(tag.id(), $ -> new STagBuilder(replace));
    }

    protected final class STagBuilder {
        private final List<TagEntry> entries = new ArrayList<>();
        private final boolean replace;

        private STagBuilder(boolean replace) {
            this.replace = replace;
        }

        public List<TagEntry> build() {
            return List.copyOf(this.entries);
        }

        public STagBuilder add(TagEntry entry) {
            this.entries.add(entry);
            return this;
        }

        public STagBuilder add(Identifier id) {
            return this.add(TagEntry.create(id));
        }

        public STagBuilder addOptional(Identifier id) {
            return this.add(TagEntry.createOptional(id));
        }

        public STagBuilder addTag(Identifier id) {
            return this.add(TagEntry.createTag(id));
        }

        public STagBuilder addOptionalTag(Identifier id) {
            return this.add(TagEntry.createOptionalTag(id));
        }

        public STagBuilder add(T element) {
            return this.add(this.getEntryKey(element));
        }

        public STagBuilder add(Supplier<T> supplier) {
            return this.add(supplier.get());
        }

        @SafeVarargs
        public final STagBuilder add(T... element) {
            Stream.of(element)
                    .map(this::getEntryKey)
                    .forEach(this::add);
            return this;
        }

        @SafeVarargs
        public final STagBuilder add(Supplier<T>... suppliers) {
            Stream.of(suppliers)
                    .map(Supplier::get)
                    .map(this::getEntryKey)
                    .forEach(this::add);
            return this;
        }

        public STagBuilder add(Registrable<T> registrable) {
            return this.add(registrable.sparklib$entryKey());
        }

        public STagBuilder add(RegistryKey<T> key) {
            return this.add(key.getValue());
        }

        public STagBuilder addOptional(RegistryKey<? extends T> registryKey) {
            return addOptional(registryKey.getValue());
        }

        public STagBuilder addTag(TagKey<T> identifiedTag) {
            return this.addTag(identifiedTag.id());
        }

        public STagBuilder addOptionalTag(TagKey<T> tag) {
            return this.addOptionalTag(tag.id());
        }

        public STagBuilder forceAddTag(TagKey<T> tag) {
            return this.add(new ForcedTagEntry(TagEntry.create(tag.id())));
        }

        public STagBuilder add(Identifier... ids) {
            for (Identifier id : ids) {
                add(id);
            }

            return this;
        }

        @SafeVarargs
        public final STagBuilder add(RegistryKey<T>... registryKeys) {
            for (RegistryKey<T> registryKey : registryKeys) {
                add(registryKey);
            }

            return this;
        }

        private RegistryKey<T> getEntryKey(T entry) {
            // FIXME
            Registry<T> registry = (Registry<T>) Registries.REGISTRIES.get(STagProvider.this.registryRef.getValue());
            if (registry != null) {
                Optional<RegistryKey<T>> key = registry.getKey(entry);

                if (key.isPresent()) {
                    return key.get();
                }
            }

            throw new UnsupportedOperationException("Adding objects is not supported by " + getClass());
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
}

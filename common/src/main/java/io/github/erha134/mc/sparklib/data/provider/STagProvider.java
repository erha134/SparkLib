package io.github.erha134.mc.sparklib.data.provider;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import io.github.erha134.easylib.string.StringFormatter;
import io.github.erha134.mc.sparklib.registry.api.Registrable;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.tag.TagEntry;
import net.minecraft.tag.TagFile;
import net.minecraft.tag.TagKey;
import net.minecraft.tag.TagManagerLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class STagProvider<T> extends AbstractTagProvider<T> {
    private final String modId;
    private final DataGenerator generator;
    private final Map<Identifier, STagBuilder> tagBuilders = new LinkedHashMap<>();

    public STagProvider(String modId,
                        DataGenerator generator,
                        Registry<T> registry) {
        super(generator, registry);
        this.modId = modId;
        this.generator = generator;

        if (!(this instanceof STagProvider.SDynamicRegistryTagProvider) && BuiltinRegistries.REGISTRIES.contains((RegistryKey) registry.getKey())) {
            throw new IllegalArgumentException("Using STagProvider to generate dynamic registry tags is not supported, Use SDynamicRegistryTagProvider instead.");
        }
    }
  
    @Override
    public abstract void configure();

    @Override
    public void run(DataWriter writer) {
        this.tagBuilders.clear();
        this.configure();
        this.tagBuilders.forEach((id, builder) -> {
            List<TagEntry> entries = builder.build()
                    .stream()
                    .filter((tag) -> !tag.canAdd(this.registry::containsId, this.tagBuilders::containsKey))
                    .toList();
            if (!entries.isEmpty()) {
                throw new IllegalArgumentException(
                        String.format(
                                Locale.ROOT,
                                "Couldn't define tag %s as it is missing following references: %s",
                                id,
                                entries.stream()
                                        .map(Objects::toString)
                                        .collect(Collectors.joining(","))));
            } else {
                JsonElement jsonElement = TagFile.CODEC.encodeStart(JsonOps.INSTANCE,
                        new TagFile(entries, builder.replace)).getOrThrow(false, log::error);
                Path path = this.generator.getOutput()
                        .resolve("data")
                        .resolve(id.getNamespace())
                        .resolve(TagManagerLoader.getPath(this.registry.getKey()))
                        .resolve(id.getPath() + ".json");

                try {
                    DataProvider.writeToPath(writer, jsonElement, path);
                } catch (IOException e) {
                    log.error("Couldn't save tags to {}", path, e);
                }
            }
        });
    }

    @Override
    public final String getName() {
        return StringFormatter.format("Tag Provider by Spark Lib ({})", this.registry.getKey().getValue());
    }

    /**
     * 请使用 {@link #getOrCreateSTagBuilder(TagKey)} 和 {@link #getOrCreateSTagBuilder(TagKey, boolean)}。
     * @deprecated
     */
    @Deprecated
    @Override
    protected ObjectBuilder<T> getOrCreateTagBuilder(TagKey<T> tag) {
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

        private RegistryKey<T> getEntryKey(T element) {
            return STagProvider.this.registry.getKey(element).orElseThrow();
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

    public abstract static class SDynamicRegistryTagProvider<T> extends STagProvider<T> {
        protected SDynamicRegistryTagProvider(String modId, DataGenerator generator, RegistryKey<? extends Registry<T>> registryKey) {
            super(modId, generator, new SimpleRegistry<>(registryKey, Lifecycle.experimental(), null) {
                @Override
                public boolean containsId(Identifier id) {
                    return true;
                }
            });
            Preconditions.checkArgument(DynamicRegistryManager.INFOS.containsKey(registryKey), "Only dynamic registries are supported in this tag provider.");
        }
    }
}

package io.github.erha134.mc.sparklib.registry;

import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import lombok.Getter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class SRegistrar {
    private static final Map<String, SRegistrar> REGISTRARS = new HashMap<>();

    @Getter
    protected final String modId;
    private boolean locked;

    protected SRegistrar(String modId) {
        this.modId = modId;
    }

    @ApiStatus.Internal
    @ExpectPlatform
    public static SRegistrar create(String modId) {
        throw new AssertionError();
    }

    public static SRegistrar getOrCreate(String modId) {
        return REGISTRARS.computeIfAbsent(modId, SRegistrar::create);
    }

    public abstract <R, T extends R> RegistryHolder<T> register(Registry<R> registry, String id, Supplier<T> supplier);

    public <R, T extends R> RegistryHolder<T> registerSimple(Registry<R> registry, String id, T value) {
        return this.register(registry, id, () -> value);
    }

    // Item

    public <I extends Item> RegistryHolder<I> item(String id, Supplier<I> supplier) {
        return this.register(Registries.ITEM, id, supplier);
    }

    public <I extends Item> RegistryHolder<I> item(String id, Function<Item.Settings, I> factory) {
        return this.item(id, () -> factory.apply(new Item.Settings()));
    }

    public RegistryHolder<Item> simpleItem(String id) {
        return this.simpleItem(id, new Item.Settings());
    }

    public RegistryHolder<Item> simpleItem(String id, UnaryOperator<Item.Settings> factory) {
        return this.simpleItem(id, factory.apply(new Item.Settings()));
    }

    public RegistryHolder<Item> simpleItem(String id, Item.Settings settings) {
        return this.simpleItem(id, new Item(settings));
    }

    public RegistryHolder<Item> simpleItem(String id, Item item) {
        return this.item(id, () -> item);
    }

    // Block

    public <B extends Block> RegistryHolder<B> block(String id, Supplier<B> supplier) {
        return this.register(Registries.BLOCK, id, supplier);
    }

    public <B extends Block> RegistryHolder<B> block(String id, Function<AbstractBlock.Settings, B> factory) {
        return this.block(id, () -> factory.apply(AbstractBlock.Settings.create()));
    }

    public RegistryHolder<Block> simpleBlock(String id) {
        return this.simpleBlock(id, AbstractBlock.Settings.create());
    }

    public RegistryHolder<Block> simpleBlock(String id, UnaryOperator<AbstractBlock.Settings> factory) {
        return this.simpleBlock(id, factory.apply(AbstractBlock.Settings.create()));
    }

    public RegistryHolder<Block> simpleBlock(String id, AbstractBlock.Settings settings) {
        return this.simpleBlock(id, new Block(settings));
    }

    public RegistryHolder<Block> simpleBlock(String id, Block block) {
        return this.block(id, () -> block);
    }

    // Item group

    public RegistryHolder<ItemGroup> group(String id, Supplier<ItemGroup> supplier) {
        return this.register(Registries.ITEM_GROUP, id, supplier);
    }

    public RegistryHolder<ItemGroup> group(String id, Text title, Supplier<ItemStack> icon, UnaryOperator<ItemGroup.Builder> factory) {
        return this.group(id, () -> factory.apply(this.createGroupBuilder(title, icon)).build());
    }

    protected abstract ItemGroup.Builder createGroupBuilder(Text title, Supplier<ItemStack> icon);

    public RegistryHolder<ItemGroup> simpleGroup(String id, Text title, ItemConvertible icon) {
        return this.simpleGroup(id, title, new ItemStack(icon));
    }

    public RegistryHolder<ItemGroup> simpleGroup(String id, Text title, ItemStack icon) {
        return this.simpleGroup(id, title, () -> icon);
    }

    public RegistryHolder<ItemGroup> simpleGroup(String id, Text title, Supplier<ItemStack> icon) {
        return this.group(id, title, icon, UnaryOperator.identity());
    }

    @Deprecated
    public RegistryHolder<ItemGroup> simpleGroup(String id, ItemGroup group) {
        return this.group(id, () -> group);
    }

    // Fluid

    public <F extends Fluid> RegistryHolder<F> fluid(String id, Supplier<F> supplier) {
        return this.register(Registries.FLUID, id, supplier);
    }

    public <F extends Fluid> RegistryHolder<F> simpleFluid(String id, F fluid) {
        return this.fluid(id, () -> fluid);
    }

    // Enchantment

    public <E extends Enchantment> RegistryHolder<E> enchantment(String id, Supplier<E> supplier) {
        return this.register(Registries.ENCHANTMENT, id, supplier);
    }

    public <E extends Enchantment> RegistryHolder<E> simpleEnchantment(String id, E enchantment) {
        return this.enchantment(id, () -> enchantment);
    }

    // Block entity type

    public <BE extends BlockEntity> RegistryHolder<BlockEntityType<BE>> blockEntity(String id, Supplier<BlockEntityType<BE>> supplier) {
        return this.register(Registries.BLOCK_ENTITY_TYPE, id, supplier);
    }

    public <BE extends BlockEntity> RegistryHolder<BlockEntityType<BE>> simpleBlockEntity(String id,
                                                                                          BlockEntityType.BlockEntityFactory<BE> factory,
                                                                                          Block... validateBlocks) {
        return this.simpleBlockEntity(id, factory, null, validateBlocks);
    }

    public <BE extends BlockEntity> RegistryHolder<BlockEntityType<BE>> simpleBlockEntity(String id,
                                                                                          BlockEntityType.BlockEntityFactory<BE> factory,
                                                                                          @Nullable Type<?> type,
                                                                                          Block... validateBlocks) {
        return this.simpleBlockEntity(id, BlockEntityType.Builder.create(factory, validateBlocks).build(type));
    }

    public <BE extends BlockEntity> RegistryHolder<BlockEntityType<BE>> simpleBlockEntity(String id, BlockEntityType<BE> type) {
        return this.blockEntity(id, () -> type);
    }

    // Entity type

    public <E extends Entity> RegistryHolder<EntityType<E>> entity(String id, Supplier<EntityType<E>> supplier) {
        return this.register(Registries.ENTITY_TYPE, id, supplier);
    }

    public <E extends Entity> RegistryHolder<EntityType<E>> simpleEntity(String id,
                                                                         EntityType.EntityFactory<E> factory,
                                                                         UnaryOperator<EntityType.Builder<E>> builder) {
        return this.simpleEntity(id, factory, SpawnGroup.MISC, builder);
    }

    public <E extends Entity> RegistryHolder<EntityType<E>> simpleEntity(String id,
                                                                         EntityType.EntityFactory<E> factory,
                                                                         SpawnGroup group,
                                                                         UnaryOperator<EntityType.Builder<E>> builder) {
        return this.simpleEntity(id, builder.apply(EntityType.Builder.create(factory, group)).build(id));
    }

    public <E extends Entity> RegistryHolder<EntityType<E>> simpleEntity(String id, EntityType<E> type) {
        return this.entity(id, () -> type);
    }

    // Custom registry

    public abstract <R> Supplier<Registry<R>> createSimpleRegistry(String id);

    public abstract <R> Supplier<DefaultedRegistry<R>> createDefaultedRegistry(String id, String defaultValueId);

    public abstract <R> void createDatapackRegistry(String id, Codec<R> codec);

    public <R> TagKey<R> createTag(Registry<R> registry, String id) {
        return TagKey.of(registry.getKey(), this.id(id));
    }

    public final void register() {
        if (this.locked) {
            throw new IllegalStateException("SRegistrar has locked");
        }

        this.locked = true;
        this.onRegister();
    }

    protected abstract void onRegister();

    protected Identifier id(String id) {
        return new Identifier(this.modId, id);
    }

    protected <R> RegistryKey<Registry<R>> registryKey(String id) {
        return RegistryKey.ofRegistry(this.id(id));
    }
}

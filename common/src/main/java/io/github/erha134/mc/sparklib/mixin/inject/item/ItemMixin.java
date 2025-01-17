package io.github.erha134.mc.sparklib.mixin.inject.item;

import dev.architectury.extensions.injected.InjectedItemExtension;
import io.github.erha134.mc.sparklib.extension.item.ItemExtensions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public abstract class ItemMixin
        implements ToggleableFeature, ItemConvertible, InjectedItemExtension, ItemExtensions {
    @Shadow
    @Deprecated
    public abstract RegistryEntry.Reference<Item> getRegistryEntry();

    @Override
    public RegistryEntry.Reference<Item> sparklib$entry() {
        return this.getRegistryEntry();
    }

    @Override
    public ItemStack sparklib$asStack() {
        return new ItemStack(this);
    }
}

package io.github.erha134.mc.sparklib.mixin.inject.block;

import dev.architectury.extensions.injected.InjectedBlockExtension;
import io.github.erha134.mc.sparklib.extension.block.BlockExtensions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockMixin
        extends AbstractBlock
        implements ItemConvertible, InjectedBlockExtension, BlockExtensions {
    @Shadow
    @Final
    private RegistryEntry.Reference<Block> registryEntry;

    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public RegistryEntry.Reference<Block> sparklib$entry() {
        return this.registryEntry;
    }

    @Override
    public Block sparklib$asBlock() {
        return (Block) (Object) this;
    }

    @Override
    public ItemStack sparklib$asStack() {
        return new ItemStack(this);
    }
}

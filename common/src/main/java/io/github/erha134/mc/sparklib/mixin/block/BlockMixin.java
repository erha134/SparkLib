package io.github.erha134.mc.sparklib.mixin.block;

import dev.architectury.extensions.injected.InjectedBlockExtension;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
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
    public RegistryEntry<Block> sparklib$registryEntry() {
        return this.registryEntry;
    }
}

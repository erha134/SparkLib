package io.github.erha134.mc.sparklib.data.provider;

import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;

@FunctionalInterface
public interface SDataProviderFactory<T extends DataProvider> {
    T create(String modId, DataOutput output);
}

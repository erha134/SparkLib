package io.github.erha134.mc.sparklib.data.factory;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

@FunctionalInterface
public interface SDataProviderFactory<T extends DataProvider> {
    T create(String modId, DataGenerator generator);
}

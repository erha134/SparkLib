package io.github.erha134.mc.sparklib.data.provider;

import lombok.Getter;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

public abstract class SDataProvider implements DataProvider {
    @Getter(onMethod_ = {@Override})
    private final String name;
    protected final String modId;
    protected final DataGenerator generator;

    public SDataProvider(String name, String modId, DataGenerator generator) {
        this.name = name;
        this.modId = modId;
        this.generator = generator;
    }
}

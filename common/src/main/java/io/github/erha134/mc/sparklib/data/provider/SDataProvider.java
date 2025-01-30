package io.github.erha134.mc.sparklib.data.provider;

import lombok.Getter;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;

public abstract class SDataProvider implements DataProvider {
    @Getter(onMethod_ = {@Override})
    private final String name;
    protected final String modId;
    protected final DataOutput output;

    public SDataProvider(String name, String modId, DataOutput output) {
        this.name = name;
        this.modId = modId;
        this.output = output;
    }
}

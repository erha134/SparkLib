package io.github.erha134.mc.sparklib.data.provider;

import io.github.erha134.easylib.string.StringFormatter;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

public abstract class SDataProvider implements DataProvider {
    private final String name;
    protected final String modId;
    protected final DataGenerator generator;

    public SDataProvider(String name, String modId, DataGenerator generator) {
        this.name = name;
        this.modId = modId;
        this.generator = generator;
    }

    @Override
    public final String getName() {
        return StringFormatter.format(this.name + " ({})", this.modId);
    }
}

package io.github.erha134.mc.sparklib.test;

import io.github.erha134.mc.sparklib.data.provider.SLanguageProvider;
import net.minecraft.data.DataOutput;

public class TestLanguageProvider extends SLanguageProvider {
    public TestLanguageProvider(String modId, DataOutput output) {
        super(modId, output, "en_us");
    }

    @Override
    public void translate(Translator translator) {
        translator.add(TestItems.TEST_1.get(), "Test Item 1");
    }
}

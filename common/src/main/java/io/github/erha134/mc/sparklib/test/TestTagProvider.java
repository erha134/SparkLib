package io.github.erha134.mc.sparklib.test;

import io.github.erha134.mc.sparklib.data.provider.STagProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TestTagProvider extends STagProvider.SItemTagProvider {
    public TestTagProvider(String modId, DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        super(modId, output, registryLookupFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateCustomTagBuilder(TestTags.TEST_ITEMS)
                .add(TestItems.TEST_1);
    }
}

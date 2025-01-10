package io.github.erha134.mc.sparklib.registry.v0.api;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.function.Supplier;

public class DoubleWrappedDeferredRegister<T1, T2> {
    protected final DeferredRegister<T1> delegate1;
    protected final DeferredRegister<T2> delegate2;

    protected DoubleWrappedDeferredRegister(String modId, Registry<T1> registry1, Registry<T2> registry2) {
        this(modId, (RegistryKey<Registry<T1>>) registry1.getKey(), (RegistryKey<Registry<T2>>) registry2.getKey());
    }

    public DoubleWrappedDeferredRegister(String modId, RegistryKey<Registry<T1>> registryKey1, RegistryKey<Registry<T2>> registryKey2) {
        this.delegate1 = DeferredRegister.create(modId, registryKey1);
        this.delegate2 = DeferredRegister.create(modId, registryKey2);
    }

    public <S1 extends T1, S2 extends T2> DoubleWrappedRegisterSupplier<S1, S2> register(String id, Supplier<S1> supplier1, Supplier<S2> supplier2) {
        return new DoubleWrappedRegisterSupplier<>(this.delegate1.register(id, supplier1),
                this.delegate2.register(id, supplier2));
    }

    public void register() {
        this.delegate1.register();
        this.delegate2.register();
    }
}

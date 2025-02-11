package com.example.test.mixin.inject;

import com.example.test.TestMod;
import com.example.test.registry.TestRegister;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(
            method = "runServer",
            at = @At("HEAD")
    )
    private void onTickStart(CallbackInfo ci) {
        TestRegister.TEST_REGISTRY.get().forEach(TestMod.LOGGER::info);
    }
}

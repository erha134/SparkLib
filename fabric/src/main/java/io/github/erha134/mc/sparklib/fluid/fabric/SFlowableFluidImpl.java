package io.github.erha134.mc.sparklib.fluid.fabric;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

public final class SFlowableFluidImpl {
    private static void platformSetup(Fluid still,
                                      Fluid flowing,
                                      Identifier stillTexture,
                                      Identifier flowingTexture,
                                      Identifier overlayTexture,
                                      int color) {
        EnvExecutor.runInEnv(Env.CLIENT, () -> () -> FluidRenderHandlerRegistry.INSTANCE.register(still,
                flowing,
                new SimpleFluidRenderHandler(stillTexture, flowingTexture, overlayTexture, color)));
    }
}

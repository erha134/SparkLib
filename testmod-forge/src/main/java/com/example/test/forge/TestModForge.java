package com.example.test.forge;

import com.example.test.TestMod;
import com.example.test.client.TestModClient;
import com.example.test.data.TestModDataGeneration;
import dev.architectury.platform.forge.EventBuses;
import io.github.erha134.mc.sparklib.data.forge.SDataGenerationForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(TestMod.MOD_ID)
public final class TestModForge {
    public TestModForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(TestMod.MOD_ID, bus);
        TestMod.init();

        if (FMLEnvironment.dist.isClient()) {
            TestModClient.initClient();
        }

        bus.addListener(this::onDataGen);
    }

    private void onDataGen(GatherDataEvent e) {
        TestModDataGeneration.initDataGen(SDataGenerationForge.create(TestMod.MOD_ID, e));
    }
}

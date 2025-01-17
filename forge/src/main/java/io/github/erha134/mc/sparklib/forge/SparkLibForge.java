package io.github.erha134.mc.sparklib.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.client.SparkLibClient;
import io.github.erha134.mc.sparklib.data.SparkLibDataGeneration;
import io.github.erha134.mc.sparklib.data.forge.SDataGenerationForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(SparkLib.MOD_ID)
public final class SparkLibForge {
    public SparkLibForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(SparkLib.MOD_ID, bus);
        SparkLib.init();

        if (FMLEnvironment.dist.isClient()) {
            SparkLibClient.initClient();
        }

        bus.addListener(this::gatherData);
    }

    private void gatherData(GatherDataEvent e) {
        SparkLibDataGeneration.initDataGen(SDataGenerationForge.create(SparkLib.MOD_ID, e));
    }
}

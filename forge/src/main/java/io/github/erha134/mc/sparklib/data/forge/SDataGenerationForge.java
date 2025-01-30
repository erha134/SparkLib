package io.github.erha134.mc.sparklib.data.forge;

import io.github.erha134.mc.sparklib.data.SDataGeneration;
import net.minecraftforge.data.event.GatherDataEvent;

public class SDataGenerationForge {
    public static SDataGeneration create(String modId, GatherDataEvent e) {
        return new SDataGeneration(modId, e.getGenerator());
    }
}

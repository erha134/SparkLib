package io.github.erha134.mc.sparklib.util.version.forge;

import net.minecraftforge.fml.ModList;

import java.net.URL;
import java.util.Optional;

public class ForgeVersionCheckerImpl {
    private static Optional<String> getUpdateUrl(String modId) {
        return ModList.get()
                .getModContainerById(modId)
                .orElseThrow()
                .getModInfo()
                .getUpdateURL()
                .map(URL::toString);
    }
}

package io.github.erha134.mc.sparklib.util.fabric;

import io.github.erha134.mc.sparklib.util.SPlatform;

public final class SPlatformImpl {
    public static SPlatform.LoaderType getLoaderType() {
        try {
            Class.forName("org.quiltmc.loader.api.QuiltLoader");
            return SPlatform.LoaderType.QUILT;
        } catch (ClassNotFoundException ignored) {
            return SPlatform.LoaderType.FABRIC;
        }
    }
}

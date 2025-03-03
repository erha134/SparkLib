package io.github.erha134.mc.sparklib.util;

import dev.architectury.injectables.annotations.ExpectPlatform;

public final class SPlatform {
    @ExpectPlatform
    public static LoaderType getLoaderType() {
        throw new AssertionError();
    }

    public enum LoaderType {
        FABRIC,
        FORGE,
        QUILT,
        NEOFORGE,
        UNKNOWN;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}

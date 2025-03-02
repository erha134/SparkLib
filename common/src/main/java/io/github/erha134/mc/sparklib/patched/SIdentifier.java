package io.github.erha134.mc.sparklib.patched;

import net.minecraft.util.Identifier;

public final class SIdentifier {
    public static Identifier create(String modId, String path) {
        return new Identifier(modId, path);
    }
}
